/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rba.jaxrs.autoconfig.core.transform;

import com.rba.jaxrs.autoconfig.core.annotations.RestApiEndpoint;
import com.rba.jaxrs.autoconfig.core.classify.ApiContext;
import com.rba.jaxrs.autoconfig.core.exceptions.ContextResolverException;
import com.rba.jaxrs.autoconfig.core.version.ApiVersion;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A default transformer that will create a context in the form of /apiversion/context1/context2
 * <p>
 * This class resolves the RestApiEndpoint information against {@link Enum} classes that implement the
 * {@link ApiVersion} and {@link ApiContext} interface.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /16/2018
 * @since 0.1.0
 */
public class DefaultRestApiEndpointTransformer implements RestApiContextTransformer {
    private final Set<Class<? extends ApiContext>> contextEnumerations = new HashSet<>();
    private final Set<Class<? extends ApiVersion>> versionEnumerations = new HashSet<>();

    /**
     * Instantiates a Default transformer.  During initialization it will find {@link ApiVersion} and {@link ApiContext}
     * implementations that are {@link Enum} classes and track those for the transform process.
     */
    public DefaultRestApiEndpointTransformer() {
        try(ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
            ClassInfoList contextClasses = scanResult.getClassesImplementing(ApiContext.class.getName());
            for (Class<?> contextClass : contextClasses.getEnums().loadClasses()) {
                //Don't need to check for enum class since we asked classGraph for enums
                contextEnumerations.add((Class<? extends ApiContext>)contextClass);
            }
            ClassInfoList versionClasses = scanResult.getClassesImplementing(ApiVersion.class.getName());
            for (Class<?> versionClass : versionClasses.getEnums().loadClasses()) {
                //Don't need to check for enum class since we asked classGraph for enums
                versionEnumerations.add((Class<? extends ApiVersion>)versionClass);
            }
        }
    }

    @Override
    public EndpointContextContainer getEndpointContext(RestApiEndpoint endpointAnnotation) {
        ApiVersion apiVersionEntry = null;
        List<ApiContext> contexts = new ArrayList<>();
        //No version is valid and defaults to empty and enabled.  If entry exists we must be able to resolve it.
        if (!endpointAnnotation.apiVersionEnumName().isEmpty()) {
            apiVersionEntry = resolveApiVersion(endpointAnnotation.apiVersionEnumName());
            if (apiVersionEntry == null) {
                throw new ContextResolverException("Unable to resolve Api Version information from the endpoint annotation for "
                        + "value: " + endpointAnnotation.apiVersionEnumName());
            }

        }
        for(String enumValueName : endpointAnnotation.apiContextEnumNames()) {
            ApiContext contextEnum = resolveApiContext(enumValueName);
            if(contextEnum == null) {
                throw new ContextResolverException("Unable to resolve Api Context information from the endpoint annotation for "
                        + "value: " + enumValueName + ". The annotation contains the following contexts to resolve: "
                        + String.join(",", endpointAnnotation.apiContextEnumNames()));
            }
            contexts.add(contextEnum);
        }
        return resolveApiPath(apiVersionEntry, contexts.toArray(new ApiContext[]{}));
    }

    @Override
    public EndpointContextContainer resolveApiPath(ApiVersion apiVersion, ApiContext... apiContexts) {
        boolean contextEnabled = true;
        StringBuilder context = new StringBuilder("");

        if(apiVersion != null) {
            contextEnabled = contextEnabled && apiVersion.isApiVersionEnabled();
            if (apiVersion.getApiVersion() != null && !apiVersion.getApiVersion().isEmpty()) {
                context.append("/").append(apiVersion.getApiVersion());
            }
        }
        if(apiContexts != null && apiContexts.length > 0) {
            for(ApiContext contextEnum : apiContexts) {
                contextEnabled = contextEnabled && contextEnum.isApiContextEnabled();
                if (contextEnum.getApiContext() != null && !contextEnum.getApiContext().isEmpty()) {
                    context.append("/").append(contextEnum.getApiContext());
                }
            }
        }
        String builtContext = context.toString();
        return new EndpointContextContainer(builtContext.length() == 0 ? "/" : builtContext, contextEnabled);
    }

    /**
     * Resolve the api version portion of the {@link RestApiEndpoint} annotation.  Use method level generics to provide a
     * level of typing to the {@link ApiVersion}.
     *
     * @param apiVersion The string name of the enum value to find
     * @param <T> A generic Enum value that implements {@link ApiVersion}
     * @return The resolved enum value or null
     */
    protected <T extends Enum<T> & ApiVersion> T resolveApiVersion(String apiVersion) {
        T versionEntry = null;
        for (Class<? extends ApiVersion> apiVersionClass : versionEnumerations) {
            try {
                versionEntry = Enum.valueOf((Class<T>) apiVersionClass, apiVersion);
                //Enum.valueOf will always throw IllegalArgumentException so no if check necessary
                break;
            } catch (IllegalArgumentException iae) {
                //We are knowingly eating this exception as we don't know how many enums exist in the system so there may
                // be more checks coming.
            }
        }
        return versionEntry;
    }

    /**
     * Resolve the api context portion of the {@link RestApiEndpoint} annotation.  Use method level generics to provide a
     * level of typing to the {@link ApiContext}.
     *
     * @param apiContext The string name of the enum value to find
     * @param <T> A generic Enum value that implements {@link ApiContext}
     * @return The resolved enum value or null
     */
    protected <T extends Enum<T> & ApiContext> T resolveApiContext(String apiContext) {
        T contextEntry = null;
        for (Class<? extends ApiContext> apiContextClass : contextEnumerations) {
            try {
                contextEntry = Enum.valueOf((Class<T>) apiContextClass, apiContext);
                //Enum.valueOf will always throw IllegalArgumentException so no if check necessary
                break;
            } catch (IllegalArgumentException iae) {
                //We are knowingly eating this exception as we don't know how many enums exist in the system so there may
                // be more checks coming.
            }
        }
        return contextEntry;
    }
}
