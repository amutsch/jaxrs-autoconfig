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

package com.rba.jaxrs.autoconfig.scan.stub;

import com.rba.jaxrs.autoconfig.annotations.RestApiEndpoint;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11/14/2018
 */
@RestApiEndpoint(apiVersionEnumName = "EXTERNAL_V1", apiContextEnumNames = "ADMIN")
@RestApiEndpoint(apiVersionEnumName = "INTERNAL", apiContextEnumNames = {"OPEN"})
public class StubEndpointWithMultipleAnnotationsForScan {
    //Empty class, annotation testing
}
