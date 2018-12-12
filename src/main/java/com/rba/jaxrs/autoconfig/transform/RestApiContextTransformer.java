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

package com.rba.jaxrs.autoconfig.transform;

import com.rba.jaxrs.autoconfig.annotations.RestApiEndpoint;
import com.rba.jaxrs.autoconfig.classify.ApiContext;
import com.rba.jaxrs.autoconfig.version.ApiVersion;

/**
 * The transformer interface is responsible for the process of transforming a single
 * {@link com.rba.jaxrs.autoconfig.annotations.RestApiEndpoint} annotation and converting it to a string context
 * representation as input into a jars server address.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /15/2018
 * @since 0.1.0
 */
public interface RestApiContextTransformer {

    /**
     * Resolves the {@link RestApiEndpoint} against enum implementations for
     * {@link com.rba.jaxrs.autoconfig.classify.ApiContext} and {@link com.rba.jaxrs.autoconfig.version.ApiVersion} to
     * create a context string and determines if that context is enabled
     *
     * @param endpointAnnotation the endpoint annotation to process into a context
     * @return the endpoint context container with the context resolved and a flag indicating whether it is enabled.
     */
    EndpointContextContainer getEndpointContext(RestApiEndpoint endpointAnnotation);

    /**
     * Uses the @{@link ApiVersion} and {@link ApiContext} to create the ApiPath.  This allows us to ensure the same
     * transforms are occurring when the rest endpoints are resolved and any other usages of the path resolving.
     *
     * @param apiVersion  the api version
     * @param apiContexts the api contexts
     * @return the endpoint context container
     */
    EndpointContextContainer resolveApiPath(ApiVersion apiVersion, ApiContext... apiContexts);
}
