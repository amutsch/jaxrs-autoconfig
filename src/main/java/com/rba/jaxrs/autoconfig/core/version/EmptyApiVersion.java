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

package com.rba.jaxrs.autoconfig.core.version;


/**
 * A default implementation of {@link ApiVersion} that provides a null value for api version and is enabled.
 *<p/>
 * This is an enum to support embedding into an annotation.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /13/2018
 * @since 0.1.0
 */
public enum EmptyApiVersion implements ApiVersion {

    EMPTY_API_VERSION;

    @Override
    public String getApiVersion() {
        return null;
    }

}
