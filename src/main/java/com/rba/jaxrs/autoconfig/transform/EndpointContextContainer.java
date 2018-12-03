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

package com.rba.jaxrs.autoconfig.transform;

import java.util.Objects;

/**
 * A container for holding the result of context transformation.  This class holds the final context and the indicator of
 * if the context is enabled.  The context will be enabled if {@link com.rba.jaxrs.autoconfig.version.ApiVersion} and all
 * {@link com.rba.jaxrs.autoconfig.classify.ApiContext} entries are enabled.  If any of the contexts are disabled the
 * resulting endpoint is resolved as disabled.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /16/2018
 * @since 0.1.0
 */
public class EndpointContextContainer {

    private final String endpointContext;
    private final boolean enabled;

    /**
     * Instantiates a new Endpoint context container with the context for the endpoint and whether it should be enabled.
     *
     * @param endpointContext the endpoint context
     * @param enabled         the enabled
     */
    public EndpointContextContainer(String endpointContext, boolean enabled) {
        this.endpointContext = endpointContext;
        this.enabled = enabled;
    }

    /**
     * returns the context that was provided to the container
     *
     * @return the endpoint context
     */
    public String getEndpointContext() {
        return endpointContext;
    }

    /**
     * A boolean flag that indicates if a jaxrs endpoint should be hosted for this context address.
     *
     * @return true if the context should be hosted by jaxrs
     */
    public boolean isEnabled() {
        return enabled;
    }

    //Override Equals and Hashcode as we are using this class in a Map Key during the scanning process.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EndpointContextContainer that = (EndpointContextContainer) o;
        return enabled == that.enabled && Objects.equals(endpointContext, that.endpointContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpointContext, enabled);
    }
}
