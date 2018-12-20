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

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.feature.StaxTransformFeature;
import org.apache.cxf.interceptor.AttachmentOutInterceptor;
import org.apache.cxf.interceptor.FIStaxOutInterceptor;
import org.apache.cxf.interceptor.FaultOutInterceptor;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.security.JAASAuthenticationFeature;
import org.apache.cxf.interceptor.transform.TransformInInterceptor;
import org.apache.cxf.jaxrs.interceptor.JAXRSDefaultFaultOutInterceptor;
import org.apache.cxf.jaxrs.interceptor.JAXRSInInterceptor;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.apache.cxf.jaxrs.provider.DataSourceProvider;
import org.apache.cxf.jaxrs.provider.SourceProvider;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.apache.cxf.transport.http.HttpAuthenticationFaultHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/12/2018
 */
public class CxfConfigurationBuilderUTEST {

    @Test
    void emptyBuilder() {
        CxfConfiguration config = new CxfConfigurationBuilder().build();
        Assertions.assertAll(() -> Assertions.assertNull(config.getBus()),
            () -> Assertions.assertEquals(0, config.getFeatures().size()),
            () -> Assertions.assertEquals(0, config.getInInterceptors().size()),
            () -> Assertions.assertEquals(0, config.getOutInterceptors().size()),
            () -> Assertions.assertEquals(0, config.getInFaultInterceptors().size()),
            () -> Assertions.assertEquals(0, config.getOutFaultInterceptors().size()),
            () -> Assertions.assertEquals(0, config.getProviders().size()),
            () -> Assertions.assertNull(config.getRootContext()),
            () -> Assertions.assertEquals(0, config.getProperties().size())
        );
    }

    @Test
    void validatePropertyShowInWadl() {
        CxfConfiguration config =
            new CxfConfigurationBuilder().setShowInWadl(false).setShowInWadl(true)
                .setShowInWadl(false).build();
        Assertions.assertAll(() -> Assertions.assertNull(config.getBus()),
            () -> Assertions.assertEquals(0, config.getFeatures().size()),
            () -> Assertions.assertEquals(0, config.getInInterceptors().size()),
            () -> Assertions.assertEquals(0, config.getOutInterceptors().size()),
            () -> Assertions.assertEquals(0, config.getInFaultInterceptors().size()),
            () -> Assertions.assertEquals(0, config.getOutFaultInterceptors().size()),
            () -> Assertions.assertEquals(0, config.getProviders().size()),
            () -> Assertions.assertNull(config.getRootContext()),
            () -> Assertions.assertEquals(1, config.getProperties().size()),
            () -> Assertions.assertTrue(((Boolean) config.getProperties().get(CxfConfigurationBuilder.PRIVATE_ENDPOINT_KEY))
                .booleanValue())
        );
    }

    @Test
    void validateAddAndSetOnBuilder() {
        Map<String, Object> props = new HashMap<>();
        props.put("key1", "UnitTest");
        props.put("Key2", "AnotherKey");
        CxfConfiguration config = new CxfConfigurationBuilder().setShowInWadl(false)
            .addFeature(new GZIPFeature())
            .addIncomingInterceptor(new TransformInInterceptor())
            .addOutgoingInterceptor(new FIStaxOutInterceptor())
            .addIncomingFaultInterceptor(new HttpAuthenticationFaultHandler())
            .addOutgoingFaultInterceptor(new HttpAuthenticationFaultHandler())
            .addProperty("RandomProperty", Boolean.TRUE)
            .addProvider(new BinaryDataProvider<>())
            .setBus(new SpringBus())
            .setFeatures(Arrays.asList(new Feature[]{new StaxTransformFeature(), new JAASAuthenticationFeature()}))
            .setIncomingInterceptors(Arrays.asList(new Interceptor[]{new JAXRSInInterceptor()}))
            .setOutgoingInterceptors(Arrays.asList(new Interceptor[]{new AttachmentOutInterceptor()}))
            .setIncomingFaultInterceptors(Arrays.asList(new Interceptor[]{new HttpAuthenticationFaultHandler()}))
            .setOutgoingFaultInterceptors(Arrays.asList(new Interceptor[]{new FaultOutInterceptor(),
                new JAXRSDefaultFaultOutInterceptor()}))
            .setProviders(Arrays.asList(new Object[]{new SourceProvider<>(), new DataSourceProvider<>()}))
            .setProperties(props)
            .setShowInWadl(false)
            .setRootContext("UnitTest")
            .build();

        Assertions.assertAll(() -> Assertions.assertTrue(config.getBus() instanceof SpringBus),
            () -> Assertions.assertEquals(3, config.getFeatures().size()),
            () -> Assertions.assertEquals(2, config.getInInterceptors().size()),
            () -> Assertions.assertEquals(2, config.getOutInterceptors().size()),
            () -> Assertions.assertEquals(2, config.getInFaultInterceptors().size()),
            () -> Assertions.assertEquals(3, config.getOutFaultInterceptors().size()),
            () -> Assertions.assertEquals(3, config.getProviders().size()),
            () -> Assertions.assertEquals("UnitTest", config.getRootContext()),
            () -> Assertions.assertEquals(4, config.getProperties().size()),
            () -> Assertions.assertTrue(((Boolean) config.getProperties().get(CxfConfigurationBuilder.PRIVATE_ENDPOINT_KEY))
                .booleanValue())
        );
    }

    @Test
    void nullSetsValidation() {
        Assertions.assertAll(() -> Assertions.assertThrows(IllegalArgumentException.class,
            () -> new CxfConfigurationBuilder().setFeatures(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().setOutgoingInterceptors(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().setProperties(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().setIncomingInterceptors(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().setProviders(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().setIncomingFaultInterceptors(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().setOutgoingFaultInterceptors(null).build())
        );

    }

    @Test
    void nullAddProperty() {
        Assertions.assertAll(() -> Assertions.assertThrows(IllegalArgumentException.class,
            () -> new CxfConfigurationBuilder().addProperty(null, "value").build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().addProperty("key", null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().addProperty(null, null).build())
        );
    }

    @Test
    void nullAddToLists() {
        Assertions.assertAll(() -> Assertions.assertThrows(IllegalArgumentException.class,
            () -> new CxfConfigurationBuilder().addProvider(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().addOutgoingInterceptor(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().addIncomingInterceptor(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().addFeature(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().addOutgoingFaultInterceptor(null).build()),
            () -> Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CxfConfigurationBuilder().addIncomingFaultInterceptor(null).build())
        );
    }
}
