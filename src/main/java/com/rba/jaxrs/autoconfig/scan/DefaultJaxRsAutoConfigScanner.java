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

package com.rba.jaxrs.autoconfig.scan;


import com.rba.jaxrs.autoconfig.annotations.RestApiEndpoint;
import com.rba.jaxrs.autoconfig.transform.DefaultRestApiEndpointTransformer;
import com.rba.jaxrs.autoconfig.transform.EndpointContextContainer;
import com.rba.jaxrs.autoconfig.transform.RestApiContextTransformer;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A default implementation of {@link JaxRsAutoConfigScanner} that will properly find all classes containing
 * {@link RestApiEndpoint} annotations whether they are on spring beans or not.  This class honors all the documentation
 * of {@link JaxRsAutoConfigScanner} in that it will honor the whitelist and blacklist.  It properly handles the usage of
 * multiple annotations being present to host a class on multiple endpoints.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11 /20/2018
 * @since 0.1.0
 */
@Component
public class DefaultJaxRsAutoConfigScanner implements JaxRsAutoConfigScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJaxRsAutoConfigScanner.class);

    //Autowiring not required as it should use the assigned default provided by the auto configuration system
    @Autowired(required = false)
    private final RestApiContextTransformer contextTransformer = new DefaultRestApiEndpointTransformer();

    @Override
    public Map<EndpointContextContainer, List<Class<?>>> getAutoConfigurationData(String... optionalScanPackages) {
        return getAutoConfigurationData(Arrays.asList(optionalScanPackages));
    }

    @Override
    public Map<EndpointContextContainer, List<Class<?>>> getAutoConfigurationData(List<String> optionalScanPackages,
        String... scanPackageBlackList) {
        Map<EndpointContextContainer, List<Class<?>>> scannedResults = new HashMap<>();
        ClassGraph endpointGraph = new ClassGraph().enableAllInfo();
        if (optionalScanPackages != null && !optionalScanPackages.isEmpty()) {
            endpointGraph = endpointGraph.whitelistPackages(optionalScanPackages.toArray(new String[]{}));
        }
        if (scanPackageBlackList.length > 0) {
            endpointGraph = endpointGraph.blacklistPackages(scanPackageBlackList);
        }
        try (ScanResult scanResult = endpointGraph.scan()) {
            processAnnotationClass(scanResult, scannedResults, RestApiEndpoint.class.getName());
            processAnnotationClass(scanResult, scannedResults, RestApiEndpoint.RestApiEndpoints.class.getName());
        }
        return scannedResults;
    }

    /**
     * This private method allows re-use of the scanResult and proper population of the data map.  The ClassGraph code
     * does not handle recognition of annotations the same as the jre.  Multiple annotations on the same class does not
     * get recognized which requires us to Scan for both {@link RestApiEndpoint} and
     * {@link com.rba.jaxrs.autoconfig.annotations.RestApiEndpoint.RestApiEndpoints} which requires multiple processing of
     * the data from the scan result.
     *
     * @param scanResult An open scanResult from calling the scan method on a ClassGraph
     * @param scannedResults The Map which is not null that data will be populated into
     * @param annotationClass The class we are calling an annotation in this process.
     */
    private void processAnnotationClass(ScanResult scanResult,
        Map<EndpointContextContainer, List<Class<?>>> scannedResults, String annotationClass) {
        try {
            ClassInfoList restApiClasses = scanResult.getClassesWithAnnotation(annotationClass);
            for (ClassInfo classData : restApiClasses) {
                RestApiEndpoint[] apiEndpoints = classData.loadClass().getAnnotationsByType(RestApiEndpoint.class);
                for (RestApiEndpoint apiEndpoint : apiEndpoints) {
                    EndpointContextContainer contextContainer = contextTransformer.getEndpointContext(apiEndpoint);
                    List<Class<?>> apiClasses = scannedResults.get(contextContainer);
                    if (apiClasses == null) {
                        apiClasses = new ArrayList<>();
                        scannedResults.put(contextContainer, apiClasses);
                    }
                    apiClasses.add(classData.loadClass());
                }
            }
        } catch (IllegalArgumentException iae) {
            //The way ClassGraph processing works there are scenarios where the annotations will not be properly found and
            // throw an exception.  Gracefully handle the exception and processing of the scenarios.  We are knowingly
            // eating the error and only logging to debug as we will run the process against the wrapping annotation.
            LOGGER.debug("No classes discovered for annotation: " + annotationClass, iae);
        }
    }
}
