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

import com.rba.jaxrs.autoconfig.core.exceptions.ContextResolverException;
import com.rba.jaxrs.autoconfig.core.scan.stub.StubEndpointWithMultipleAnnotationsForScan;
import com.rba.jaxrs.autoconfig.stubs.StubEndpointEmptyValuesInAnnotation;
import com.rba.jaxrs.autoconfig.stubs.StubEndpointWithEmptyAnnotation;
import com.rba.jaxrs.autoconfig.stubs.StubEndpointWithMultipleAnnotations;
import com.rba.jaxrs.autoconfig.stubs.StubEndpointWithNullContexts;
import com.rba.jaxrs.autoconfig.stubs.StubEndpointWithVersionAndMultipleContexts;
import com.rba.jaxrs.autoconfig.stubs.StubEndpointWithVersionAndSingleContext;
import com.rba.jaxrs.autoconfig.core.transform.EndpointContextContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11/19/2018
 */
class JaxRsAutoConfigScannerUTEST {

    private final JaxRsAutoConfigScanner scanner = new DefaultJaxRsAutoConfigScanner();

    //This will error due to invalid contexts in the noscan stubs package
    @Test
    void verifyAutoConfigScanNoScanBase() {
        Assertions.assertThrows(ContextResolverException.class,
            () -> scanner.getAutoConfigurationData());
    }

    //Verify scanning a package with no annotations in it
    @Test
    void verifyAutoConfigScanNoBeansInScanBase() {
        Map<EndpointContextContainer, List<Class<?>>> results = scanner.getAutoConfigurationData("com.rba.jaxrs.autoconfig"
            + ".classify");
        Assertions.assertTrue(results.isEmpty());
    }

    //Scan a small package that has 1 class with 2 annotations
    @Test
    void verifyAutoConfigNarrowScanBase() {
        Map<EndpointContextContainer, List<Class<?>>> results = scanner.getAutoConfigurationData("com.rba.jaxrs.autoconfig"
            + ".core.scan.stub");
        Map<EndpointContextContainer, List<Class<?>>> expectedResults = createScanPackageResults();
        validateResults(results, expectedResults);
    }

    //This will return everything in the project excluding the invalid Rest Annotations
    @Test
    void verifyBlacklistOnlyScan() {
        Map<EndpointContextContainer, List<Class<?>>> results =
            scanner.getAutoConfigurationData((List<String>)null, "com.rba.jaxrs.autoconfig.stubs.noscan");
        Map<EndpointContextContainer, List<Class<?>>> expectedResults = createScanPackageResults();
        expectedResults.putAll(createStubPackageResults());
        validateResults(results, expectedResults);
    }

    @Test
    void verifyCombinedWhitelistAndBlacklistScan() {
        Map<EndpointContextContainer, List<Class<?>>> results =
            scanner.getAutoConfigurationData(Collections.singletonList("com.rba.jaxrs.autoconfig.stubs"), "com.rba.jaxrs"
                + ".autoconfig.stubs.noscan");
        Map<EndpointContextContainer, List<Class<?>>> expectedResults = createStubPackageResults();
        validateResults(results, expectedResults);
    }

    private static Map<EndpointContextContainer, List<Class<?>>> createScanPackageResults() {
        Map<EndpointContextContainer, List<Class<?>>> expectedResults = new HashMap<>();
        expectedResults.put(new EndpointContextContainer("/v1/admin", true),
            Collections.singletonList(StubEndpointWithMultipleAnnotationsForScan.class));
        expectedResults.put(new EndpointContextContainer("/open", true),
            Collections.singletonList(StubEndpointWithMultipleAnnotationsForScan.class));
        return expectedResults;
    }

    private static Map<EndpointContextContainer, List<Class<?>>> createStubPackageResults() {
        Map<EndpointContextContainer, List<Class<?>>> expectedResults = new HashMap<>();
        expectedResults.put(new EndpointContextContainer("/", true),
            Arrays.asList(StubEndpointEmptyValuesInAnnotation.class,
                StubEndpointWithEmptyAnnotation.class, StubEndpointWithNullContexts.class));
        expectedResults.put(new EndpointContextContainer("/v1/open", true),
            Arrays.asList(StubEndpointWithMultipleAnnotations.class,
                StubEndpointWithVersionAndSingleContext.class));
        expectedResults.put(new EndpointContextContainer("/open/testing", false),
            Collections.singletonList(StubEndpointWithMultipleAnnotations.class));
        expectedResults.put(new EndpointContextContainer("/v2/open/admin/testing", false),
            Collections.singletonList(StubEndpointWithVersionAndMultipleContexts.class));
        return expectedResults;
    }

    private static void validateResults(Map<EndpointContextContainer, List<Class<?>>> results, Map<EndpointContextContainer,
        List<Class<?>>> expectedResults) {
        Assertions.assertEquals(expectedResults.size(), results.size());
        //Use our expected results to drive the validation.  If there is a difference(missing/different) it will occur in
        // this block since the size matches.
        for (Map.Entry<EndpointContextContainer, List<Class<?>>> mapEntry : expectedResults.entrySet()) {
            Assertions.assertTrue(results.containsKey(mapEntry.getKey()));
            List<Class<?>> resultClassList = results.get(mapEntry.getKey());
            List<Class<?>> expectedClassList = mapEntry.getValue();
            Assertions.assertEquals(expectedClassList.size(), resultClassList.size());
            for (Class<?> expectedClass : expectedClassList) {
                Assertions.assertTrue(resultClassList.contains(expectedClass));
            }
        }
    }
}
