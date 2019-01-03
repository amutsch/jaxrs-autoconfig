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

package com.rba.jaxrs.autoconfig.core.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/19/2018
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmptyPropertiesUTEST.PropertyConfig.class)
@ActiveProfiles("propcheck")
public class EmptyPropertiesUTEST {

    @Autowired
    AutoConfigProperties properties;

    @Test
    public void verifyEmptyProperties() {
        Assertions.assertNotNull(properties);
        Assertions.assertEquals(0, properties.getPackagesToBlacklist().size());
        Assertions.assertEquals(0, properties.getPackagesToScan().size());
    }

    @SpringBootApplication
    @EnableConfigurationProperties(AutoConfigProperties.class)
    static class PropertyConfig {
        //Just Need Property annotations
    }
}
