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

package com.rba.jaxrs.autoconfig.core.annotations;

import com.rba.jaxrs.autoconfig.cxf.config.CxfSpringConfiguration;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation that will enable configuration of CXF if it finds the factory on the classpath and the application is a
 * web application.  The auto configuration will use the {@link RestApiEndpoint} to setup and configure
 * {@link JAXRSServerFactoryBean} with services and create one for each distinct base bath.
 *
 *
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/11/2018
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import(CxfSpringConfiguration.class)
@ConditionalOnClass(JAXRSServerFactoryBean.class)
@ConditionalOnWebApplication
public @interface EnableJaxrsConfig {
    //No Code specific to the annotation
}
