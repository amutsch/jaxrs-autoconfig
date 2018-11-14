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

package com.rba.jaxrs.autoconfig.version;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Runs basic validations to ensure the EmptyApiVersion properly meets the ApiVersion expectations and defaults come through.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11/13/2018
 */
public class EmptyApiVersionUTEST {

    @Test
    public void validateApiVersion() {
        Assertions.assertNull(EmptyApiVersion.EMPTY_API_VERSION.getApiVersion(), "The API Version should always be null");
    }

    @Test
    public void validateEmptyApiVersionEnabled() {
        Assertions.assertTrue(EmptyApiVersion.EMPTY_API_VERSION.isApiVersionEnabled(), "This should always be true from the "
                + "default of the interface");
    }
}
