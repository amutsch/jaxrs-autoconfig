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

package com.rba.jaxrs.autoconfig.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for indicating that a class is a rest endpoint.  Enums that implement the
 * {@link com.rba.jaxrs.autoconfig.version.ApiVersion} interface and {@link com.rba.jaxrs.autoconfig.classify.ApiContext}
 * interfaces should be used for inserting details into the RestApiEndpoint annotation.
 * <p/>
 * Scanners that setup the Rest Endpoints will search for enums that implement the interfaces and resolve information
 * against the data that is found.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /14/2018
 * @since 0.1.0
 */
@Repeatable(RestApiEndpoint.RestApiEndpoints.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestApiEndpoint {

    /**
     * A string value that represents an enum value in an enum that implements the
     * {@link com.rba.jaxrs.autoconfig.version.ApiVersion} interface.
     *
     * @return the string
     */
    String apiVersionEnumName() default "EMPTY_API_VERSION";

    /**
     * An array of string values that represent values inside of 1 or more enums that implement the
     * {@link com.rba.jaxrs.autoconfig.classify.ApiContext} interface.
     *
     * @return the string [ ]
     */
    String[] apiContextEnumNames() default{"EMPTY_API_CONTEXT"};

    /**
     * The RestApiEndpoints annotation allows the definition of multiple @{@link RestApiEndpoint} annotations to be used
     * on the same class.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface RestApiEndpoints {
        /**
         * The {@link RestApiEndpoint} annotations as a list.
         *
         * @return the rest api endpoint [ ]
         */
        RestApiEndpoint[] value() default{};
    }
}
