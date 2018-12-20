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

package com.rba.jaxrs.autoconfig.cxf.config;

import com.rba.jaxrs.autoconfig.core.scan.JaxRsAutoConfigScanner;
import com.rba.jaxrs.autoconfig.core.transform.EndpointContextContainer;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.service.factory.ServiceConstructionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/19/2018
 */
public class CxfJaxrsInitializer implements ApplicationContextAware, DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(CxfSpringConfiguration.class);

    private final JaxRsAutoConfigScanner scanner;

    private final List<String> basePackages;

    private final List<String> blacklist;

    private final ObjectProvider<CxfServerFactoryCustomizer> cxfCustomizers;

    private ApplicationContext appContext;

    public CxfJaxrsInitializer(JaxRsAutoConfigScanner scanner,
        ObjectProvider<CxfServerFactoryCustomizer> factoryCustomizers,
        List<String> packagesToScan, List<String> packagesNotToScan) {
        this.scanner = scanner;
        this.cxfCustomizers = factoryCustomizers;
        basePackages = packagesToScan;
        blacklist = packagesNotToScan;
    }

    public void initializeCxfEndpoints() {
        Map<EndpointContextContainer, List<Class<?>>> configurationData;
        if (blacklist != null && !blacklist.isEmpty()) {
            configurationData = scanner.getAutoConfigurationData(
                basePackages, blacklist.toArray(new String[]{}));
        } else {
            configurationData = scanner.getAutoConfigurationData(basePackages);
        }
        AutowireCapableBeanFactory beanFactory = appContext.getAutowireCapableBeanFactory();
        //The scanner will have collapsed any paths that boil down to the same actual address so we only need to process
        // each map entry.
        for (Map.Entry<EndpointContextContainer, List<Class<?>>> mapEntry : configurationData.entrySet()) {
            if (mapEntry.getKey().isEnabled() && !mapEntry.getValue().isEmpty()) {

                List<Object> resourceBeans = new ArrayList<>();
                for (Class<?> resourceClass : mapEntry.getValue()) {
                    Object bean = beanFactory.createBean(resourceClass, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
                    resourceBeans.add(bean);
                }
                if (!resourceBeans.isEmpty()) {
                    JAXRSServerFactoryBean cxfFactoryBean = new JAXRSServerFactoryBean();
                    cxfFactoryBean.setAddress(mapEntry.getKey().getEndpointContext());
                    cxfFactoryBean.setServiceBeans(resourceBeans);
                    if (cxfCustomizers != null) {
                        cxfCustomizers.stream().sorted().forEach((customizer) -> customizer.customize(cxfFactoryBean));
                    }
                    try {
                        beanFactory.initializeBean(cxfFactoryBean.create(),
                            "cxfserver" + cxfFactoryBean.getAddress().replaceAll("/", "-"));
                    }catch(ServiceConstructionException sce) {
                        String resources = resourceBeans.stream()
                            .map(expectedBean -> expectedBean.getClass().getSimpleName())
                            .collect(Collectors.joining());
                        LOG.warn("Error creating Cxf Jaxrs Server that was expected to contain service beans: " + resources);
                    }
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
        initializeCxfEndpoints();
    }

    @Override
    public void destroy() throws Exception {

    }
}
