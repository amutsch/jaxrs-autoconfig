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
import com.rba.jaxrs.autoconfig.core.transform.DefaultRestApiEndpointTransformer;
import com.rba.jaxrs.autoconfig.cxf.builder.CxfConfigurationBuilder;
import com.rba.jaxrs.autoconfig.cxf.config.CxfServerFactoryCustomizer;
import com.rba.jaxrs.autoconfig.stubs.ApiContextTestImpl;
import com.rba.jaxrs.autoconfig.stubs.ApiVersionTestImpl;
import org.apache.cxf.interceptor.security.JAASAuthenticationFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/14/2018
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CxfServerFactoryBaseWiringUTEST.NoTransformerConfig.class)
public class CxfServerFactoryBaseWiringUTEST {

    @Qualifier("applyToAllCustomizer")
    @Autowired
    CxfServerFactoryCustomizer applyToAllCustomizer;

    @Qualifier("disabledEndpointCustomizer")
    @Autowired
    CxfServerFactoryCustomizer disabledEndpointCustomizer;

    @Test
    void verifyTransformerAutowire() {
        Assertions.assertEquals(DefaultRestApiEndpointTransformer.class.getSimpleName(),
                disabledEndpointCustomizer.getContextTransformer().getClass().getSimpleName());
    }

    @Test
    void customizerThatMatchesButIsDisabledNotApplied() {
        List<CxfServerFactoryCustomizer> customizers = new ArrayList<>();
        customizers.add(disabledEndpointCustomizer);
        customizers.add(applyToAllCustomizer);

        JAXRSServerFactoryBean cxfBean = new JAXRSServerFactoryBean();
        cxfBean.setAddress(applyToAllCustomizer.getContextTransformer().resolveApiPath(ApiVersionTestImpl.EXTERNAL_V2,
            ApiContextTestImpl.ADMIN).getEndpointContext());

        customizers.stream().sorted().forEach(customizer -> customizer.customize(cxfBean));

        Assertions.assertAll(() -> Assertions.assertEquals(1, cxfBean.getInInterceptors().size()),
            () -> Assertions.assertEquals(GZIPInInterceptor.class.getName(),
                cxfBean.getInInterceptors().get(0).getClass().getName()),
            () -> Assertions.assertEquals(1, cxfBean.getOutInterceptors().size()),
            () -> Assertions.assertEquals(GZIPOutInterceptor.class.getName(),
                cxfBean.getOutInterceptors().get(0).getClass().getName()),
            () -> Assertions.assertEquals(0, cxfBean.getProviders().size()),
            () -> Assertions.assertEquals(0, cxfBean.getFeatures().size()));
    }

    @Configuration
    static class NoTransformerConfig {

        @Bean
        public CxfServerFactoryCustomizer applyToAllCustomizer() {
            CxfConfigurationBuilder builder = new CxfConfigurationBuilder();
            builder.setRootContext("UnitTest");
            builder.addIncomingInterceptor(new GZIPInInterceptor());
            builder.addOutgoingInterceptor(new GZIPOutInterceptor());
            return new CxfServerFactoryCustomizer(builder.build(), true, null);
        }

        @Bean
        public CxfServerFactoryCustomizer disabledEndpointCustomizer() {
            CxfConfigurationBuilder builder = new CxfConfigurationBuilder();
            builder.setRootContext("UnitTest");
            builder.addFeature(new JAASAuthenticationFeature());
            builder.addProvider(new JacksonJaxbJsonProvider());
            CxfServerFactoryCustomizer customizer = new CxfServerFactoryCustomizer(builder.build(), false,
                ApiVersionTestImpl.EXTERNAL_V2, ApiContextTestImpl.ADMIN);
            return customizer;
        }
    }
}
