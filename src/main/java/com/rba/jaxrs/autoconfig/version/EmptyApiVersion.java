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

import com.sun.istack.internal.Nullable;

/**
 * A default implementation of {@link ApiVersion} that provides a null value for api version and is enabled.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /13/2018
 */
public class EmptyApiVersion implements ApiVersion {

    /**
     * The constant EMPTY_API_VERSION.
     */
    public static final EmptyApiVersion EMPTY_API_VERSION = new EmptyApiVersion();

    //Private EmptyApiVersion constructor since version and enabled can not be changed, force use of the static EMPTY_API_VERSION
    private EmptyApiVersion() {}

    @Nullable
    @Override
    public String getApiVersion() {
        return null;
    }

}
