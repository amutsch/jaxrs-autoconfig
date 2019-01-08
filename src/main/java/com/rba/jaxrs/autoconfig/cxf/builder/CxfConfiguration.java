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
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Message;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Encapsulates a CxfConfiguration for customizing the {@link org.apache.cxf.jaxrs.JAXRSServerFactoryBean}  It is
 * recommended to use the {@link CxfConfigurationBuilder} for creating the CxfConfiguration as that has null safety checks
 * and {@link IllegalArgumentException} for unexpected nulls.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /27/2018
 */
public class CxfConfiguration implements InterceptorProvider {

    private final String rootContext;
    private final Bus bus;
    private final List<Feature> features;
    private final List<Interceptor<?>> incomingInterceptors;
    private final List<Interceptor<?>> outgoingInterceptors;
    private final List<Interceptor<?>> incomingFaultInterceptors;
    private final List<Interceptor<?>> outgoingFaultInterceptors;
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
        List<Interceptor<?>> outgoingInterceptors, List<Interceptor<?>> incomingFaultInterceptors,
        List<Interceptor<?>> outgoingFaultInterceptors, List<Object> providers, Map<String, Object> properties) {
        this.rootContext = rootContext;
        this.bus = bus;
        this.features = features;
        this.incomingInterceptors = incomingInterceptors;
        this.outgoingInterceptors = outgoingInterceptors;
        this.incomingFaultInterceptors = incomingFaultInterceptors;
        this.outgoingFaultInterceptors = outgoingFaultInterceptors;
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

    @Override
    public List<Interceptor<? extends Message>> getInInterceptors() {
        return incomingInterceptors;
    }

    @Override
    public List<Interceptor<? extends Message>> getOutInterceptors() {
        return outgoingInterceptors;
    }

    @Override
    public List<Interceptor<? extends Message>> getInFaultInterceptors() {
        return incomingFaultInterceptors;
    }

    @Override
    public List<Interceptor<? extends Message>> getOutFaultInterceptors() {
        return outgoingFaultInterceptors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CxfConfiguration that = (CxfConfiguration) o;
        return Objects.equals(rootContext, that.rootContext)
            && Objects.equals(bus, that.bus)
            && features.equals(that.features)
            && incomingInterceptors.equals(that.incomingInterceptors)
            && outgoingInterceptors.equals(that.outgoingInterceptors)
            && incomingFaultInterceptors.equals(that.incomingFaultInterceptors)
            && outgoingFaultInterceptors.equals(that.outgoingFaultInterceptors)
            && providers.equals(that.providers)
            && properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootContext, bus,
            features.isEmpty() ? null : features,
            incomingInterceptors.isEmpty() ? null : incomingInterceptors,
            outgoingInterceptors.isEmpty() ? null : outgoingInterceptors,
            incomingFaultInterceptors.isEmpty() ? null : incomingFaultInterceptors,
            outgoingFaultInterceptors.isEmpty() ? null : outgoingFaultInterceptors,
            providers.isEmpty() ? null : providers,
            properties.isEmpty() ? null : properties);
    }
}
