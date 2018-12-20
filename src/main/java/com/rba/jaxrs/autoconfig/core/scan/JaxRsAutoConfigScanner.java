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

package com.rba.jaxrs.autoconfig.core.scan;

import com.rba.jaxrs.autoconfig.core.annotations.RestApiEndpoint;
import com.rba.jaxrs.autoconfig.core.transform.RestApiContextTransformer;
import com.rba.jaxrs.autoconfig.core.transform.EndpointContextContainer;

import java.util.List;
import java.util.Map;

/**
 * Implementations of this interface are responsible for finding the classes with
 * {@link RestApiEndpoint} annotations in the system.  The scanner is also
 * responsible for running the scanned results through the
 * {@link RestApiContextTransformer}.
 * The results Map should allow the rest factories to make all the decisions and have all the data necessary for create of
 * rest apis.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /19/2018
 * @since 0.1.0
 */
public interface JaxRsAutoConfigScanner {

    /**
     * Scans the requested packages for classes annotated with the
     * {@link RestApiEndpoint}.  The Rest Endpoints should be transformed and return
     * with their relevant context.
     *
     * An Empty map should be returned if no annotations are found.
     *
     * @param optionalScanPackages optional application packages to scan
     * @return the auto configuration data map
     */
    Map<EndpointContextContainer, List<Class<?>>> getAutoConfigurationData(String... optionalScanPackages);

    /**
     * Scans the requested packages for classes annotated with the
     * {@link RestApiEndpoint}.  The Rest Endpoints should be transformed and return
     * with their relevant context.  The black list packages will be excluded even if they are a sub package of the
     * whitelist.
     *
     * An Empty map should be returned if no annotations are found.
     *
     * @param optionalScanPackages optional application packages to scan
     * @param scanPackageBlackList optional application packages to blacklist from scanning
     * @return the auto configuration data map
     */
    Map<EndpointContextContainer, List<Class<?>>> getAutoConfigurationData(List<String> optionalScanPackages,
            String... scanPackageBlackList);
}
