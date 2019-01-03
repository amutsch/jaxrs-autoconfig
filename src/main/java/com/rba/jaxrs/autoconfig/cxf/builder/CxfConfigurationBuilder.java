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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Builder for collecting the customizations to apply to the @{@link org.apache.cxf.jaxrs.JAXRSServerFactoryBean}
 * <p>
 * The builder will produce a {@link CxfConfiguration} when the build method is invoked.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /27/2018
 */
public class CxfConfigurationBuilder {

    private final List<Feature> features = new ArrayList<>();
    private Bus bus;
    private final List<Interceptor<?>> incomingInterceptors = new ArrayList<>();
    private final List<Interceptor<?>> outgoingInterceptors = new ArrayList<>();
    private final List<Interceptor<?>> incomingFaultInterceptors = new ArrayList<>();
    private final List<Interceptor<?>> outgoingFaultInterceptors = new ArrayList<>();
    private final List<Object> providers = new ArrayList<>();
    private final Map<String, Object> properties = new HashMap<>();
    private String rootContext;

    /**
     * The constant PRIVATE_ENDPOINT_KEY.
     */
    public static final String PRIVATE_ENDPOINT_KEY = "org.apache.cxf.endpoint.private";

    /**
     * Sets root context.
     *
     * @param rootContext the root context
     * @return the cxf configuration builder
     */
    public CxfConfigurationBuilder setRootContext(String rootContext) {
        this.rootContext = rootContext;
        return this;
    }

    /**
     * Showing an endpoint in a wadl is controlled by a property.  This method will add or remove that property allowing
     * you to control if information for an jaxrs server and the services in it show in the wadl.
     *
     * @param showInWadl the show in wadl
     * @return cxf configuration builder
     */
    public CxfConfigurationBuilder setShowInWadl(boolean showInWadl) {
        if(showInWadl) {
            //This is default so remove from properties
            properties.remove(PRIVATE_ENDPOINT_KEY);
        } else {
            properties.put(PRIVATE_ENDPOINT_KEY, Boolean.TRUE);
        }
        return this;
    }

    /**
     * sets the bus that should be used by the {@link org.apache.cxf.jaxrs.JAXRSServerFactoryBean}.  In most systems this
     * isn't required as there is only one bus.
     *
     * @param bus the bus
     * @return cxf configuration builder
     */
    public CxfConfigurationBuilder setBus(Bus bus) {
        this.bus = bus;
        return this;
    }

    /**
     * Adds the list of features to our current ongoing list of features.
     *
     * @param features the features
     * @return cxf configuration builder
     * @throws IllegalArgumentException when a null list of features is input
     */
    public CxfConfigurationBuilder setFeatures(List<Feature> features) {
        if (features == null) {
            throw new IllegalArgumentException("Feature list can not be null");
        }
        this.features.addAll(features);
        return this;
    }

    /**
     * Adds additional feature to the feature list
     *
     * @param feature the feature
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null features is input
     */
    public CxfConfigurationBuilder addFeature(Feature feature) {
        if (feature == null) {
            throw new IllegalArgumentException("feature can not be null");
        }
        this.features.add(feature);
        return this;
    }

    /**
     * Adds all the interceptors to the incoming interceptor list
     *
     * @param incomingInterceptors the incoming interceptors
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null list of interceptors is input
     */
    public CxfConfigurationBuilder setIncomingInterceptors(List<Interceptor<?>> incomingInterceptors) {
        if (incomingInterceptors == null) {
            throw new IllegalArgumentException("Interceptor list can not be null");
        }
        this.incomingInterceptors.addAll(incomingInterceptors);
        return this;
    }

    /**
     * Add incoming interceptor to the incoming interceptors list
     *
     * @param incomingInterceptor the incoming interceptor
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null interceptor is input
     */
    public CxfConfigurationBuilder addIncomingInterceptor(Interceptor<?> incomingInterceptor) {
        if (incomingInterceptor == null) {
            throw new IllegalArgumentException("Interceptor can not be null");
        }
        this.incomingInterceptors.add(incomingInterceptor);
        return this;
    }

    /**
     * Adds all the interceptors into the outgoing interceptor list
     *
     * @param outgoingInterceptors the outgoing interceptors
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null interceptor list is input
     */
    public CxfConfigurationBuilder setOutgoingInterceptors(List<Interceptor<?>> outgoingInterceptors) {
        if (outgoingInterceptors == null) {
            throw new IllegalArgumentException("Interceptor list can not be null");
        }
        this.outgoingInterceptors.addAll(outgoingInterceptors);
        return this;
    }

    /**
     * Add outgoing interceptor to the outgoing interceptor list
     *
     * @param outgoingInterceptor the outgoing interceptor
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null interceptor is input
     */
    public CxfConfigurationBuilder addOutgoingInterceptor(Interceptor<?> outgoingInterceptor) {
        if (outgoingInterceptor == null) {
            throw new IllegalArgumentException("Interceptor can not be null");
        }
        this.outgoingInterceptors.add(outgoingInterceptor);
        return this;
    }

    /**
     * Adds all the interceptors to the incoming fault interceptor list
     *
     * @param incomingFaultInterceptors the incoming interceptors
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null list of interceptors is input
     */
    public CxfConfigurationBuilder setIncomingFaultInterceptors(List<Interceptor<?>> incomingFaultInterceptors) {
        if (incomingFaultInterceptors == null) {
            throw new IllegalArgumentException("Interceptor list can not be null");
        }
        this.incomingFaultInterceptors.addAll(incomingFaultInterceptors);
        return this;
    }

    /**
     * Add incoming interceptor to the incoming fault interceptors list
     *
     * @param incomingFaultInterceptor the incoming interceptor
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null interceptor is input
     */
    public CxfConfigurationBuilder addIncomingFaultInterceptor(Interceptor<?> incomingFaultInterceptor) {
        if (incomingFaultInterceptor == null) {
            throw new IllegalArgumentException("Interceptor can not be null");
        }
        this.incomingFaultInterceptors.add(incomingFaultInterceptor);
        return this;
    }

    /**
     * Adds all the interceptors into the outgoing fault interceptor list
     *
     * @param outgoingFaultInterceptors the outgoing interceptors
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null interceptor list is input
     */
    public CxfConfigurationBuilder setOutgoingFaultInterceptors(List<Interceptor<?>> outgoingFaultInterceptors) {
        if (outgoingFaultInterceptors == null) {
            throw new IllegalArgumentException("Interceptor list can not be null");
        }
        this.outgoingFaultInterceptors.addAll(outgoingFaultInterceptors);
        return this;
    }

    /**
     * Add outgoing interceptor to the outgoing fault interceptor list
     *
     * @param outgoingFaultInterceptor the outgoing interceptor
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null interceptor is input
     */
    public CxfConfigurationBuilder addOutgoingFaultInterceptor(Interceptor<?> outgoingFaultInterceptor) {
        if (outgoingFaultInterceptor == null) {
            throw new IllegalArgumentException("Interceptor can not be null");
        }
        this.outgoingFaultInterceptors.add(outgoingFaultInterceptor);
        return this;
    }

    /**
     * Adds all providers to the existing list of providers
     *
     * @param providers the providers
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null list of providers is input
     */
    public CxfConfigurationBuilder setProviders(List<Object> providers) {
        if (providers == null) {
            throw new IllegalArgumentException("Provider list can not be null");
        }
        this.providers.addAll(providers);
        return this;
    }

    /**
     * Add provider to the existing list of providers
     *
     * @param provider the provider
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null provider is input
     */
    public CxfConfigurationBuilder addProvider(Object provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider can not be null");
        }
        this.providers.add(provider);
        return this;
    }

    /**
     * Adds all the properties from the map into the current property list
     *
     * @param properties the properties
     * @return the configuration builder
     * @throws IllegalArgumentException when a null property map is input
     */
    public CxfConfigurationBuilder setProperties(Map<String, Object> properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties can not be null");
        }
        this.properties.putAll(properties);
        return this;
    }

    /**
     * Adds a new property to the property map
     *
     * @param key   the key
     * @param value the value
     * @return the cxf configuration builder
     * @throws IllegalArgumentException when a null key or value is input
     */
    public CxfConfigurationBuilder addProperty(String key, Object value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("Both key and value are required");
        }
        this.properties.put(key, value);
        return this;
    }

    /**
     * Creates a {@link CxfConfiguration} object based on the various builder properties that were set.
     *
     * @return the cxf configuration
     */
    public CxfConfiguration build() {
        return new CxfConfiguration(rootContext, bus, features, incomingInterceptors, outgoingInterceptors,
            incomingFaultInterceptors, outgoingFaultInterceptors, providers, properties);
    }
}
