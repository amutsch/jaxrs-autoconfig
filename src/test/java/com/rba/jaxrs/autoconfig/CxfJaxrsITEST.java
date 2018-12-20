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
import com.rba.jaxrs.autoconfig.core.annotations.EnableJaxrsConfig;
import com.rba.jaxrs.autoconfig.core.properties.AutoConfigProperties;
import com.rba.jaxrs.autoconfig.cxf.builder.CxfConfigurationBuilder;
import com.rba.jaxrs.autoconfig.cxf.config.CxfServerFactoryCustomizer;
import com.rba.jaxrs.autoconfig.stubs.ApiContextTestImpl;
import com.rba.jaxrs.autoconfig.stubs.ApiVersionTestImpl;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/19/2018
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CxfJaxrsITEST.CxfJaxrsApplication.class, webEnvironment =
    SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public class CxfJaxrsITEST {

    @Autowired
    private AutoConfigProperties properties;

    @Test
    public void wireTest() {
        Assertions.assertNotNull(properties);
        Assertions.assertEquals("com.rba.jaxrs.autoconfig.stubs.noscan", properties.getPackagesToBlacklist().get(0));
    }

    @SpringBootApplication
    @EnableJaxrsConfig
    static class CxfJaxrsApplication {

        public static void main(String[] args) {
            SpringApplication.run(CxfJaxrsApplication.class, args);
        }

        @Bean
        public SpringBus cxf() {
            SpringBus bus = new SpringBus();
            bus.setFeatures(Arrays.asList(new Feature[]{new LoggingFeature()}));
            return bus;
        }

        @DependsOn("cxf")
        @Bean
        public CxfServerFactoryCustomizer allEndpointCustomizer() {
            CxfConfigurationBuilder cxfConfig = new CxfConfigurationBuilder()
                .setBus(cxf())
                .addProvider(new JacksonJaxbJsonProvider());
            return new CxfServerFactoryCustomizer(cxfConfig.build(), true, null);
        }

        @Bean
        public CxfServerFactoryCustomizer openApiCustomizer() {
            CxfConfigurationBuilder cxfConfig = new CxfConfigurationBuilder()
                .addFeature(new GZIPFeature());
            return new CxfServerFactoryCustomizer(cxfConfig.build(), false, ApiVersionTestImpl.EXTERNAL_V1,
                ApiContextTestImpl.OPEN);
        }
    }
}
