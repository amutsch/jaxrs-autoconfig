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

import com.rba.jaxrs.autoconfig.core.classify.ApiContext;

/**
 * A special {@link ApiContext} geared directly towards versioning.  Broken into its own
 * interface so we can force a single version context to a single endpoint.
 * <p>
 * Interface that indicates a version segment of an api url.  The version can be enabled/disabled and the context can be a string
 * or null/empty.  It is expected that factories creating the jaxrs servers will honor this enabled flag.
 *</p>
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /13/2018
 * @since 0.1
 */
public interface ApiVersion {

    /**
     * Gets the api version context as a string.
     *
     * @return the api version
     */
    String getApiVersion();

    /**
     * Returns if the api version is enabled and should be hosted by a jaxrs server.
     * Defaults to True
     *
     * @return the boolean
     */
    default boolean isApiVersionEnabled() {
        return true;
    }
}
