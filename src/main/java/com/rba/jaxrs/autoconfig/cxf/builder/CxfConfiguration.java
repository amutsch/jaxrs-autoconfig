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

package com.rba.jaxrs.autoconfig.cxf.builder;

import org.apache.cxf.Bus;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.interceptor.Interceptor;

import java.util.List;
import java.util.Map;

/**
 * Encapsulates a CxfConfiguration for customizing the {@link org.apache.cxf.jaxrs.JAXRSServerFactoryBean}  It is
 * recommended to use the {@link CxfConfigurationBuilder} for creating the CxfConfiguration as that has null safety checks
 * and {@link IllegalArgumentException} for unexpected nulls.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /27/2018
 */
public class CxfConfiguration {

    private final String rootContext;
    private final Bus bus;
    private final List<Feature> features;
    private final List<Interceptor<?>> incomingInterceptors;
    private final List<Interceptor<?>> outgoingInterceptors;
    private final List<Object> providers;
    private final Map<String, Object> properties;

    /**
     * Instantiates a new Cxf configuration.
     *
     * @param rootContext          the root context
     * @param bus                  the bus
     * @param features             the features
     * @param incomingInterceptors the incoming interceptors
     * @param outgoingInterceptors the outgoing interceptors
     * @param providers            the providers
     * @param properties           the properties
     */
    public CxfConfiguration(String rootContext, Bus bus, List<Feature> features, List<Interceptor<?>> incomingInterceptors,
        List<Interceptor<?>> outgoingInterceptors, List<Object> providers, Map<String, Object> properties) {
        this.rootContext = rootContext;
        this.bus = bus;
        this.features = features;
        this.incomingInterceptors = incomingInterceptors;
        this.outgoingInterceptors = outgoingInterceptors;
        this.providers = providers;
        this.properties = properties;
    }

    /**
     * Gets root context.
     *
     * @return the root context
     */
    public String getRootContext() {
        return rootContext;
    }

    /**
     * Gets bus.
     *
     * @return the bus
     */
    public Bus getBus() {
        return bus;
    }

    /**
     * Gets features.
     *
     * @return the features
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * Gets incoming interceptors.
     *
     * @return the incoming interceptors
     */
    public List<Interceptor<?>> getIncomingInterceptors() {
        return incomingInterceptors;
    }

    /**
     * Gets outgoing interceptors.
     *
     * @return the outgoing interceptors
     */
    public List<Interceptor<?>> getOutgoingInterceptors() {
        return outgoingInterceptors;
    }

    /**
     * Gets providers.
     *
     * @return the providers
     */
    public List<Object> getProviders() {
        return providers;
    }

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public Map<String, Object> getProperties() {
        return properties;
    }
}
