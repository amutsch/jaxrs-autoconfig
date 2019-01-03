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

import com.rba.jaxrs.autoconfig.core.annotations.RestApiEndpoint;
import com.rba.jaxrs.autoconfig.core.exceptions.ContextResolverException;
import com.rba.jaxrs.autoconfig.stubs.noscan.StubEndpointInvalidContext;
import com.rba.jaxrs.autoconfig.stubs.noscan.StubEndpointInvalidVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11/16/2018
 */
public class DefaultRestApiEndpointTransformerUTEST {
    private final RestApiContextTransformer transformer = new DefaultRestApiEndpointTransformer();

    @Test
    public void invalidContextThrowsException() {
        RestApiEndpoint[] annotations = StubEndpointInvalidContext.class.getAnnotationsByType(RestApiEndpoint.class);
        Assertions.assertEquals(1, annotations.length);
        ContextResolverException cre = Assertions.assertThrows(ContextResolverException.class,
            () -> transformer.getEndpointContext(annotations[0]));
        Assertions.assertTrue(cre.getMessage().contains("INVALID_CONTEXT"));
        Assertions.assertTrue(cre.getMessage().endsWith(String.join(",", annotations[0].apiContextEnumNames())));
    }

    @Test
    public void invalidVersionThrowsException() {
        RestApiEndpoint[] annotations = StubEndpointInvalidVersion.class.getAnnotationsByType(RestApiEndpoint.class);
        Assertions.assertEquals(1, annotations.length);
        ContextResolverException cre = Assertions.assertThrows(ContextResolverException.class,
            () -> transformer.getEndpointContext(annotations[0]));
        Assertions.assertTrue(cre.getMessage().endsWith(annotations[0].apiVersionEnumName()));
    }
}
