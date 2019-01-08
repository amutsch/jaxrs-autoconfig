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

package com.rba.jaxrs.autoconfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.rba.jaxrs.autoconfig.core.transform.RestApiContextTransformer;
import com.rba.jaxrs.autoconfig.cxf.builder.CxfConfiguration;
import com.rba.jaxrs.autoconfig.cxf.builder.CxfConfigurationBuilder;
import com.rba.jaxrs.autoconfig.cxf.config.CxfServerFactoryCustomizer;
import com.rba.jaxrs.autoconfig.stubs.ApiContextTestImpl;
import com.rba.jaxrs.autoconfig.stubs.ApiVersionTestImpl;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.feature.StaxTransformFeature;
import org.apache.cxf.interceptor.AttachmentInInterceptor;
import org.apache.cxf.interceptor.AttachmentOutInterceptor;
import org.apache.cxf.interceptor.FIStaxOutInterceptor;
import org.apache.cxf.interceptor.FaultOutInterceptor;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.security.JAASAuthenticationFeature;
import org.apache.cxf.interceptor.transform.TransformInInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.interceptor.JAXRSDefaultFaultOutInterceptor;
import org.apache.cxf.jaxrs.interceptor.JAXRSInInterceptor;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.apache.cxf.jaxrs.provider.DataSourceProvider;
import org.apache.cxf.jaxrs.provider.SourceProvider;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.apache.cxf.transport.http.HttpAuthenticationFaultHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/14/2018
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CxfServerFactoryUTEST.UnitTestConfig.class)
public class CxfServerFactoryUTEST {

    @Qualifier("applyToAllCustomizer")
    @Autowired
    CxfServerFactoryCustomizer applyToAllCustomizer;

    @Qualifier("duplicateCustomizer")
    @Autowired
    CxfServerFactoryCustomizer duplicateCustomizer;

    @Qualifier("externalAdminCustomizer")
    @Autowired
    CxfServerFactoryCustomizer externalAdminCustomizer;

    @Qualifier("fullCustomizer")
    @Autowired
    CxfServerFactoryCustomizer fullCustomizer;

    @Test
    void verifyCustomTransformerAutowire() {
        Assertions.assertAll(() -> Assertions.assertEquals(TestTransformer.class.getSimpleName(),
            applyToAllCustomizer.getContextTransformer().getClass().getSimpleName()),
            () -> Assertions.assertEquals(TestTransformer.class.getSimpleName(),
                duplicateCustomizer.getContextTransformer().getClass().getSimpleName()),
            () -> Assertions.assertEquals(TestTransformer.class.getSimpleName(),
                externalAdminCustomizer.getContextTransformer().getClass().getSimpleName())
        );
    }

    @Test
    void verifyCustomizerOrder() {
        List<CxfServerFactoryCustomizer> customizers = new ArrayList<>();
        customizers.add(externalAdminCustomizer);
        customizers.add(applyToAllCustomizer);
        customizers.add(duplicateCustomizer);
        final AtomicInteger applyToAllCount = new AtomicInteger();
        customizers.stream().sorted().forEach(
            customizer -> {
                //We have 2 applyToAllEndpoint customizers so if we get one that is not apply to all before we see those 2
                // then fail
                if(!customizer.isApplyToAllEndpoints() && applyToAllCount.get() < 2) {
                    Assertions.fail("Expected ApplyToAll customizers to be ordered first");
                }
                applyToAllCount.incrementAndGet();
            }
        );
    }

    @Test
    public void onlyApplyToAllCustomizersApplied() {
        JAXRSServerFactoryBean cxfBean =
            setupAndRunCustomizers(applyToAllCustomizer.getContextTransformer().resolveApiPath(ApiVersionTestImpl.EXTERNAL_V2,
            ApiContextTestImpl.OPEN).getEndpointContext());

        Assertions.assertAll(() -> Assertions.assertEquals(1, cxfBean.getInInterceptors().size()),
            () -> Assertions.assertEquals(GZIPInInterceptor.class.getName(),
                cxfBean.getInInterceptors().get(0).getClass().getName()),
            () -> Assertions.assertEquals(1, cxfBean.getOutInterceptors().size()),
            () -> Assertions.assertEquals(GZIPOutInterceptor.class.getName(),
                cxfBean.getOutInterceptors().get(0).getClass().getName()),
            () -> Assertions.assertEquals(1, cxfBean.getProviders().size()),
            () -> Assertions.assertEquals(1, cxfBean.getFeatures().size()),
            () -> Assertions.assertEquals(applyToAllCustomizer.getCxfConfiguration().getBus(), cxfBean.getBus()),
            () -> Assertions.assertNotEquals(fullCustomizer.getCxfConfiguration().getBus(), cxfBean.getBus()),
            () -> Assertions.assertEquals(0, cxfBean.getProperties(true).size()),
            () -> Assertions.assertEquals(1, cxfBean.getOutFaultInterceptors().size()),
            () -> Assertions.assertEquals(1, cxfBean.getInFaultInterceptors().size())
        );
    }

    @Test
    public void applyToAllAndAdmin() {
        JAXRSServerFactoryBean cxfBean = setupAndRunCustomizers(
            externalAdminCustomizer.getContextTransformer().resolveApiPath(ApiVersionTestImpl.EXTERNAL_V1,
            ApiContextTestImpl.ADMIN).getEndpointContext());

        Assertions.assertAll(() -> Assertions.assertEquals(2, cxfBean.getInInterceptors().size()),
            () -> Assertions.assertEquals(GZIPInInterceptor.class.getName(),
                cxfBean.getInInterceptors().get(0).getClass().getName()),
            () -> Assertions.assertEquals(2, cxfBean.getOutInterceptors().size()),
            () -> Assertions.assertEquals(GZIPOutInterceptor.class.getName(),
                cxfBean.getOutInterceptors().get(0).getClass().getName()),
            () -> Assertions.assertEquals(2, cxfBean.getProviders().size()),
            () -> Assertions.assertEquals(2, cxfBean.getFeatures().size()),
            () -> Assertions.assertEquals(applyToAllCustomizer.getCxfConfiguration().getBus(), cxfBean.getBus()),
            () -> Assertions.assertNotEquals(fullCustomizer.getCxfConfiguration().getBus(), cxfBean.getBus()),
            () -> Assertions.assertEquals(0, cxfBean.getProperties(true).size()),
            () -> Assertions.assertEquals(1, cxfBean.getOutFaultInterceptors().size()),
            () -> Assertions.assertEquals(1, cxfBean.getInFaultInterceptors().size())
        );
    }

    @Test
    public void applyToAllAndFullCustomizerForOpen() {
        JAXRSServerFactoryBean cxfBean =
            setupAndRunCustomizers(applyToAllCustomizer.getContextTransformer().resolveApiPath(ApiVersionTestImpl.EXTERNAL_V1,
            ApiContextTestImpl.OPEN).getEndpointContext());

        Assertions.assertAll(() -> Assertions.assertEquals(3, cxfBean.getInInterceptors().size()),
            () -> Assertions.assertEquals(GZIPInInterceptor.class.getName(),
                cxfBean.getInInterceptors().get(0).getClass().getName()),
            () -> Assertions.assertEquals(3, cxfBean.getOutInterceptors().size()),
            () -> Assertions.assertEquals(GZIPOutInterceptor.class.getName(),
                cxfBean.getOutInterceptors().get(0).getClass().getName()),
            () -> Assertions.assertEquals(4, cxfBean.getProviders().size()),
            () -> Assertions.assertEquals(4, cxfBean.getFeatures().size()),
            () -> Assertions.assertNotEquals(applyToAllCustomizer.getCxfConfiguration().getBus(), cxfBean.getBus()),
            () -> Assertions.assertEquals(fullCustomizer.getCxfConfiguration().getBus(), cxfBean.getBus()),
            () -> Assertions.assertEquals(4, cxfBean.getProperties(true).size()),
            () -> Assertions.assertTrue((Boolean)cxfBean.getProperties().get(CxfConfigurationBuilder.PRIVATE_ENDPOINT_KEY)),
            () -> Assertions.assertEquals(4, cxfBean.getOutFaultInterceptors().size()),
            () -> Assertions.assertEquals(3, cxfBean.getInFaultInterceptors().size())
        );
    }

    private JAXRSServerFactoryBean setupAndRunCustomizers(String address) {
        List<CxfServerFactoryCustomizer> customizers = new ArrayList<>();
        customizers.add(externalAdminCustomizer);
        customizers.add(applyToAllCustomizer);
        customizers.add(duplicateCustomizer);
        customizers.add(fullCustomizer);

        //This address doesn't match any of the specified customizers
        JAXRSServerFactoryBean cxfBean = new JAXRSServerFactoryBean();
        cxfBean.setAddress(address);

        customizers.stream().sorted().forEach(customizer -> customizer.customize(cxfBean));

        return cxfBean;
    }

    @Configuration
    @ComponentScan(basePackages = "com.rba.jaxrs.autoconfig.core")
    static class UnitTestConfig {

        @Bean
        public RestApiContextTransformer contextTransformer() {
            return new TestTransformer();
        }

        @Bean
        public GZIPOutInterceptor gzipOutInterceptor() {
            return new GZIPOutInterceptor();
        }

        @Bean
        public GZIPInInterceptor gzipInInterceptor() {
            return new GZIPInInterceptor();
        }

        @Bean
        public JacksonJaxbJsonProvider getProvider() {
            return new JacksonJaxbJsonProvider();
        }

        @Bean
        public GZIPFeature getGzipFeature() {
            return new GZIPFeature();
        }

        @Bean
        public FaultOutInterceptor getFaultOutInterceptor() {
            return new FaultOutInterceptor();
        }

        @Bean
        public LoggingInInterceptor getIncomingFaultInterceptor() {
            return new LoggingInInterceptor();
        }

        @Bean
        public CxfServerFactoryCustomizer applyToAllCustomizer() {
            CxfConfigurationBuilder builder = new CxfConfigurationBuilder();
            builder.setRootContext("UnitTest");
            builder.setBus(new SpringBus());
            builder.addIncomingInterceptor(gzipInInterceptor());
            builder.addOutgoingInterceptor(gzipOutInterceptor());
            builder.addProvider(getProvider());
            builder.addFeature(getGzipFeature());
            builder.addOutgoingFaultInterceptor(getFaultOutInterceptor());
            builder.addIncomingFaultInterceptor(getIncomingFaultInterceptor());
            return new CxfServerFactoryCustomizer(builder.build(), true, null);
        }

        @Bean
        public CxfServerFactoryCustomizer duplicateCustomizer() {
            CxfConfigurationBuilder builder = new CxfConfigurationBuilder();
            builder.setRootContext("UnitTest");
            builder.addIncomingInterceptor(gzipInInterceptor());
            builder.addOutgoingInterceptor(gzipOutInterceptor());
            builder.addProvider(getProvider());
            builder.addFeature(getGzipFeature());
            builder.addOutgoingFaultInterceptor(getFaultOutInterceptor());
            builder.addIncomingFaultInterceptor(getIncomingFaultInterceptor());
            return new CxfServerFactoryCustomizer(builder.build(), true, null);
        }

        @Bean
        public CxfServerFactoryCustomizer externalAdminCustomizer() {
            CxfConfigurationBuilder builder = new CxfConfigurationBuilder();
            builder.setRootContext("UnitTest");
            builder.addIncomingInterceptor(gzipInInterceptor());
            builder.addOutgoingInterceptor(gzipOutInterceptor());
            builder.addIncomingInterceptor(new AttachmentInInterceptor());
            builder.addOutgoingInterceptor(new AttachmentOutInterceptor());
            builder.addFeature(new JAASAuthenticationFeature());
            builder.addProvider(new JacksonJaxbJsonProvider());
            return new CxfServerFactoryCustomizer(builder.build(), false, ApiVersionTestImpl.EXTERNAL_V1,
                ApiContextTestImpl.ADMIN);
        }

        @Bean
        public CxfServerFactoryCustomizer fullCustomizer() {
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

            return new CxfServerFactoryCustomizer(config, false, ApiVersionTestImpl.EXTERNAL_V1,
                ApiContextTestImpl.OPEN);
        }
    }
}
