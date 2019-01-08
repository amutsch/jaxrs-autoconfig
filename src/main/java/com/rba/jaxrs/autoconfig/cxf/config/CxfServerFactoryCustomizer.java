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

package com.rba.jaxrs.autoconfig.cxf.config;

import com.rba.jaxrs.autoconfig.core.classify.ApiContext;
import com.rba.jaxrs.autoconfig.core.customizer.BootCustomizer;
import com.rba.jaxrs.autoconfig.core.transform.DefaultRestApiEndpointTransformer;
import com.rba.jaxrs.autoconfig.core.transform.EndpointContextContainer;
import com.rba.jaxrs.autoconfig.core.transform.RestApiContextTransformer;
import com.rba.jaxrs.autoconfig.core.version.ApiVersion;
import com.rba.jaxrs.autoconfig.cxf.builder.CxfConfiguration;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/11/2018
 */
public class CxfServerFactoryCustomizer implements BootCustomizer<JAXRSServerFactoryBean>,
    Comparable<CxfServerFactoryCustomizer> {

    //Autowiring not required as it should use the assigned default provided by the auto configuration system
    @Autowired(required = false)
    private RestApiContextTransformer contextTransformer = new DefaultRestApiEndpointTransformer();

    private final CxfConfiguration cxfConfig;
    private final boolean applyToAllEndpoints;
    private final ApiVersion apiVersion;
    private final ApiContext[] apiContexts;

    public CxfServerFactoryCustomizer(CxfConfiguration cxfConfig, boolean applyToAllEndpoints,
        ApiVersion apiVersion, ApiContext... contexts) {
        if (cxfConfig == null) {
            throw new IllegalArgumentException("Cxf configuration is required for a customizer");
        }
        this.cxfConfig = cxfConfig;
        this.applyToAllEndpoints = applyToAllEndpoints;
        this.apiVersion = apiVersion;
        this.apiContexts = contexts;
    }

    @Override
    public void customize(JAXRSServerFactoryBean factory) {
        EndpointContextContainer contextContainer = contextTransformer.resolveApiPath(apiVersion, apiContexts);
        if (factory != null
            && (applyToAllEndpoints
            || (contextContainer.isEnabled()
            && contextContainer.getEndpointContext().equals(factory.getAddress())))
        ) {
            //Since calling getBus on the JAXRSServerFactoryBean will create a bus if needed always set the bus if we have
            // one
            if (cxfConfig.getBus() != null) {
                factory.setBus(cxfConfig.getBus());
            }
            //Root Context will be setting up and application
            //if(cxfConfig.getRootContext())
            for (Feature feature : cxfConfig.getFeatures()) {
                if (!factory.getFeatures().contains(feature)) {
                    factory.getFeatures().add(feature);
                }
            }
            for (Interceptor<?> interceptor : cxfConfig.getInInterceptors()) {
                if (!factory.getInInterceptors().contains(interceptor)) {
                    factory.getInInterceptors().add(interceptor);
                }

            }
            for (Interceptor<?> interceptor : cxfConfig.getOutInterceptors()) {
                if (!factory.getOutInterceptors().contains(interceptor)) {
                    factory.getOutInterceptors().add(interceptor);
                }
            }
            for (Interceptor<?> interceptor : cxfConfig.getInFaultInterceptors()) {
                if (!factory.getInFaultInterceptors().contains(interceptor)) {
                    factory.getInFaultInterceptors().add(interceptor);
                }

            }
            for (Interceptor<?> interceptor : cxfConfig.getOutFaultInterceptors()) {
                if (!factory.getOutFaultInterceptors().contains(interceptor)) {
                    factory.getOutFaultInterceptors().add(interceptor);
                }
            }
            //Properties are using a last one wins strategy as we will order the customizers
            factory.getProperties(true).putAll(cxfConfig.getProperties());

            for (Object provider : cxfConfig.getProviders()) {
                if (!factory.getProviders().contains(provider)) {
                    factory.setProvider(provider);
                }
            }

        }
    }

    public RestApiContextTransformer getContextTransformer() {
        return contextTransformer;
    }

    public CxfConfiguration getCxfConfiguration() {
        return cxfConfig;
    }

    public boolean isApplyToAllEndpoints() {
        return applyToAllEndpoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CxfServerFactoryCustomizer that = (CxfServerFactoryCustomizer) o;
        return applyToAllEndpoints == that.applyToAllEndpoints
            && cxfConfig.equals(that.cxfConfig)
            && ((apiVersion == null && that.apiVersion == null)
                || (apiVersion != null && apiVersion.equals(that.apiVersion)))
            && Arrays.equals(apiContexts, that.apiContexts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cxfConfig, applyToAllEndpoints, apiVersion, Arrays.asList(apiContexts));
    }

    @Override
    public int compareTo(CxfServerFactoryCustomizer other) {
        if (other == null) {
            return -1;
        }
        if (applyToAllEndpoints && !other.applyToAllEndpoints) {
            return -1;
        } else if (!applyToAllEndpoints && other.applyToAllEndpoints) {
            return 1;
        }
        return 0;
    }
}
