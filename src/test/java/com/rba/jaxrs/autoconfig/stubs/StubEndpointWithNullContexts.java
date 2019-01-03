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

package com.rba.jaxrs.autoconfig.stubs;

import com.rba.jaxrs.autoconfig.core.annotations.RestApiEndpoint;

import javax.ws.rs.Path;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11/14/2018
 */
@RestApiEndpoint(apiVersionEnumName = "NULL_VERSION", apiContextEnumNames = "NULL_CONTEXT")
@Path("/nullcontexts")
public class StubEndpointWithNullContexts extends TestEndpoint {
    //Empty class, annotation testing
}
