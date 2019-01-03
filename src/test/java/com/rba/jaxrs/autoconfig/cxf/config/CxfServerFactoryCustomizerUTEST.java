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

import com.rba.jaxrs.autoconfig.cxf.builder.CxfConfigurationBuilder;
import com.rba.jaxrs.autoconfig.stubs.ApiContextTestImpl;
import com.rba.jaxrs.autoconfig.stubs.ApiVersionTestImpl;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/13/2018
 */
public class CxfServerFactoryCustomizerUTEST {

    @Test
    void nullFactoryIsNotCustomized() {
        CxfServerFactoryCustomizer customizer = new CxfServerFactoryCustomizer(new CxfConfigurationBuilder().build(), true,
            null);
        JAXRSServerFactoryBean factory = null;
        customizer.customize(factory);
        Assertions.assertNull(factory);
    }

    @Test
    void nullCxfConfigInConstructor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CxfServerFactoryCustomizer(null, true, null));
    }

    @Test
    void nullCompareTo() {
        CxfServerFactoryCustomizer customizer = new CxfServerFactoryCustomizer(new CxfConfigurationBuilder().build(), true,
            null);
        Assertions.assertEquals(-1, customizer.compareTo(null));
    }

    @Test
    void equalsValidation() {
        CxfServerFactoryCustomizer customizer = new CxfServerFactoryCustomizer(new CxfConfigurationBuilder().build(), false,
            ApiVersionTestImpl.EXTERNAL_V1, ApiContextTestImpl.AUTHENTICATED, ApiContextTestImpl.ADMIN);
        CxfServerFactoryCustomizer customizer2 = new CxfServerFactoryCustomizer(new CxfConfigurationBuilder().build(), false,
            ApiVersionTestImpl.EXTERNAL_V1, ApiContextTestImpl.AUTHENTICATED, ApiContextTestImpl.ADMIN);
        Assertions.assertAll(() -> Assertions.assertEquals(customizer, customizer),
            () -> Assertions.assertTrue(customizer.equals(customizer2)),
            () -> Assertions.assertNotEquals(customizer, Boolean.TRUE));
    }

    @Test
    void hashcodeValidation() {
        CxfServerFactoryCustomizer customizer = new CxfServerFactoryCustomizer(new CxfConfigurationBuilder().build(), false,
            ApiVersionTestImpl.EXTERNAL_V1, ApiContextTestImpl.AUTHENTICATED, ApiContextTestImpl.ADMIN);
        CxfServerFactoryCustomizer customizer2 = new CxfServerFactoryCustomizer(new CxfConfigurationBuilder().build(), false,
            ApiVersionTestImpl.EXTERNAL_V1, ApiContextTestImpl.AUTHENTICATED, ApiContextTestImpl.ADMIN);
        Assertions.assertEquals(customizer.hashCode(), customizer.hashCode());
        Assertions.assertEquals(customizer.hashCode(), customizer2.hashCode());
    }
}
