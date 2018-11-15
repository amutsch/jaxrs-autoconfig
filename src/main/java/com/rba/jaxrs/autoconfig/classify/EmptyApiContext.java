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

package com.rba.jaxrs.autoconfig.classify;

import com.sun.istack.internal.Nullable;

/**
 * A default implementation of {@link ApiContext} that provides a null value for api context and is enabled.
 * <p/>
 * This is an enum to support embedding into an annotation.
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /14/2018
 * @Since 0.1.0
 */
public enum EmptyApiContext implements ApiContext {

    EMPTY_API_CONTEXT;

    @Nullable
    @Override
    public String getApiContext() {
        return null;
    }

}
