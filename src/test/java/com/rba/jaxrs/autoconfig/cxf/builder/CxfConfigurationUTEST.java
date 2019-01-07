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

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.interceptor.FaultOutInterceptor;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/20/2018
 */
public class CxfConfigurationUTEST {

    @Test
    public void equalsValidation() {
        CxfConfiguration config = new CxfConfigurationBuilder().build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().build();
        Assertions.assertAll(() -> Assertions.assertEquals(config, config),
            () -> Assertions.assertEquals(config, config2),
            () -> Assertions.assertEquals(config.hashCode(), config2.hashCode()));
    }

    @Test
    public void nullIsNotEqual() {
        Assertions.assertFalse(new CxfConfigurationBuilder().build().equals(null));
    }

    @Test
    public void wrongClassNotEqual() {
        Assertions.assertFalse(new CxfConfigurationBuilder().build().equals("Definitely not same class"));
    }

    @Test
    public void contextEqualsCheck() {
        CxfConfiguration config = new CxfConfigurationBuilder().setRootContext("JUNIT").build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().setRootContext("JUNIT").build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder().setRootContext("JUNIT-DIFF").build();
        completeAssertions(config, config2, notEqualConfig);
    }

    @Test
    public void busEqualsCheck() {
        Bus bus = new SpringBus();
        CxfConfiguration config = new CxfConfigurationBuilder().setBus(bus).build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().setBus(bus).build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder().setBus(new SpringBus()).build();
        completeAssertions(config, config2, notEqualConfig);
    }

    @Test
    public void featuresEqualCheck() {
        Feature feature = new GZIPFeature();
        CxfConfiguration config = new CxfConfigurationBuilder().addFeature(feature).build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().addFeature(feature).build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder().addFeature(new LoggingFeature()).build();
        completeAssertions(config, config2, notEqualConfig);
    }

    @Test
    public void incomingInterceptorsCheck() {
        Interceptor<?> interceptor = new GZIPInInterceptor();
        CxfConfiguration config = new CxfConfigurationBuilder().addIncomingInterceptor(interceptor).build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().addIncomingInterceptor(interceptor).build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder()
            .addIncomingInterceptor(new LoggingInInterceptor()).build();
        completeAssertions(config, config2, notEqualConfig);
    }

    @Test
    public void outgoingInterceptorsCheck() {
        Interceptor<?> interceptor = new GZIPOutInterceptor();
        CxfConfiguration config = new CxfConfigurationBuilder().addOutgoingInterceptor(interceptor).build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().addOutgoingInterceptor(interceptor).build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder()
            .addOutgoingInterceptor(new LoggingOutInterceptor()).build();
        completeAssertions(config, config2, notEqualConfig);
    }

    @Test
    public void incomingFaultInterceptorsCheck() {
        Interceptor<?> interceptor = new GZIPInInterceptor();
        CxfConfiguration config = new CxfConfigurationBuilder().addIncomingFaultInterceptor(interceptor).build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().addIncomingFaultInterceptor(interceptor).build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder()
            .addIncomingFaultInterceptor(new LoggingInInterceptor()).build();
        completeAssertions(config, config2, notEqualConfig);
    }

    @Test
    public void outgoingFaultInterceptorsCheck() {
        Interceptor<?> interceptor = new FaultOutInterceptor();
        CxfConfiguration config = new CxfConfigurationBuilder().addOutgoingFaultInterceptor(interceptor).build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().addOutgoingFaultInterceptor(interceptor).build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder()
            .addOutgoingFaultInterceptor(new LoggingOutInterceptor()).build();
        completeAssertions(config, config2, notEqualConfig);
    }

    @Test
    public void providersCheck() {
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        CxfConfiguration config = new CxfConfigurationBuilder().addProvider(provider).build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().addProvider(provider).build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder().addProvider(new JacksonJsonProvider()).build();
        completeAssertions(config, config2, notEqualConfig);
    }

    @Test
    public void propertiesCheck() {
        CxfConfiguration config = new CxfConfigurationBuilder().setShowInWadl(false).build();
        CxfConfiguration config2 = new CxfConfigurationBuilder().setShowInWadl(false).build();
        CxfConfiguration notEqualConfig = new CxfConfigurationBuilder().addProperty("Property", Boolean.FALSE).build();
        completeAssertions(config, config2, notEqualConfig);
    }

    private void completeAssertions(CxfConfiguration baseConfig, CxfConfiguration equalConfig,
        CxfConfiguration notEqualConfig) {
        Assertions.assertAll(() -> Assertions.assertEquals(baseConfig, baseConfig),
            () -> Assertions.assertEquals(baseConfig, equalConfig),
            () -> Assertions.assertEquals(baseConfig.hashCode(), equalConfig.hashCode()),
            () -> Assertions.assertNotEquals(baseConfig, notEqualConfig),
            () -> Assertions.assertNotEquals(baseConfig.hashCode(), notEqualConfig.hashCode()));
    }

}
