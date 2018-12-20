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

import com.rba.jaxrs.autoconfig.core.classify.ApiContext;
import com.rba.jaxrs.autoconfig.core.transform.DefaultRestApiEndpointTransformer;
import com.rba.jaxrs.autoconfig.core.transform.EndpointContextContainer;
import com.rba.jaxrs.autoconfig.core.version.ApiVersion;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/14/2018
 */
public class TestTransformer extends DefaultRestApiEndpointTransformer {


    @Override
    public EndpointContextContainer resolveApiPath(ApiVersion apiVersion, ApiContext... apiContexts) {
        String path = "/";
        if(apiContexts != null) {
            for (ApiContext apiContext : apiContexts) {
                path = String.join("/", path, apiContext.getApiContext());
            }
        }
        if(apiVersion != null) {
            path = String.join("/", path, apiVersion.getApiVersion());
        }
        return new EndpointContextContainer(path.replaceAll("//", "/"), true);
    }
}
