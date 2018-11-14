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

/**
 * An interface for passing context through to the JaxRS server creation.
 *<p>
 * Usages may include a role context or indicators such as internal/external or possibly context by subsystems.
 *</p>
 * <p>
 * Supports enabling and disabling the context since there are use cases where context availability may be data driven or
 * system parameter driven or both.  The transformation of this interface to a jaxrs server path will honor the enabled flag.
 *</p>
 * Example Usage:
 *
 * <code>
 *     public enum SubSystemContext implements ApiContext {
 *         SUBSYSTEM_A("contexta", "subsys.enabled.a"),
 *         SUBSYSTEM_B("contextb", "subsys.enabled.b");
 *
 *         private final String apiContext;
 *         private final String environmentParam;
 *
 *         SybSystemContext(String apiContext, String environmentParam) {
 *              this.apiContext = apiContext;
 *              this.environmentParam = environmentParam;
 *         }
 *
 *         public String getApiContext() {
 *             return apiContext;
 *         }
 *
 *         public boolean isApiContextEnabled() {
 *             String paramValue = System.getProperty(environmentParam);
 *             return paramValue == null ? true || Boolean.valueOf(paramValue);
 *         }
 *     }
 * </code>
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /14/2018
 * @since 0.1.0
 */
public interface ApiContext {

    /**
     * returns a string representation of a part of the api path.
     *
     * @return the api context fragment
     */
    String getApiContext();

    /**
     * Returns if the context fragment is enabled and should be hosted by a jaxrs server.
     * Defaults to True
     *
     * @return the true if this api version is enabled
     */
    default boolean isApiContextEnabled() {
        return true;
    }
}
