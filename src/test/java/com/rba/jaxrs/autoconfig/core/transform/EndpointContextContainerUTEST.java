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

package com.rba.jaxrs.autoconfig.core.transform;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11/20/2018
 */
public class EndpointContextContainerUTEST {

    @Test
    void nullCompareCheck() {
        Assertions.assertFalse(new EndpointContextContainer("/", false).equals(null));
    }

    @Test
    void sameItemCheck() {
        EndpointContextContainer container = new EndpointContextContainer("/", true);
        Assertions.assertTrue(container.equals(container));
    }

    @Test
    void differentEnabledFlag() {
        EndpointContextContainer container1 = new EndpointContextContainer("/", true);
        EndpointContextContainer container2 = new EndpointContextContainer("/", false);
        Assertions.assertFalse(container1.equals(container2));
    }

    @Test
    void differentContextAddress() {
        EndpointContextContainer container1 = new EndpointContextContainer("/", true);
        EndpointContextContainer container2 = new EndpointContextContainer("/v1", true);
        Assertions.assertFalse(container1.equals(container2));
    }

    @Test
    void differentItemSameDetails() {
        EndpointContextContainer container1 = new EndpointContextContainer("/v1", true);
        EndpointContextContainer container2 = new EndpointContextContainer("/v1", true);
        Assertions.assertTrue(container1.equals(container2));
        Assertions.assertEquals(container1.hashCode(), container2.hashCode());
    }

    @Test
    void differentClassCheck() {
        EndpointContextContainer container1 = new EndpointContextContainer("/v1", true);
        Assertions.assertFalse(container1.equals("String Data Compare"));
    }
}
