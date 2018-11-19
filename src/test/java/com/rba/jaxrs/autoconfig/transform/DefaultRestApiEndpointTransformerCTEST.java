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

import com.rba.jaxrs.autoconfig.annotations.RestApiEndpoint;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Runs our tests that simulate the same action with iterating over {@link RestApiEndpoint} annotations and expecting
 * positive results.
 *
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11/16/2018
 */
public class DefaultRestApiEndpointTransformerCTEST {
    private String stubClassName;
    private RestApiEndpoint[] restApiAnnotations;
    private final RestApiContextTransformer transformer = new DefaultRestApiEndpointTransformer();

    @Given("^stub class \"([^\"]*)\" with annotation$")
    public void stubClassName(String className) {
        this.stubClassName = className;
    }

    @When("^I inspect the class annotations$")
    public void inspectAnnotatedClass() throws ClassNotFoundException {
        restApiAnnotations = Class.forName(stubClassName).getAnnotationsByType(RestApiEndpoint.class);

    }

    @Then("^I should be told \"([^\"]*)\" and \"([^\"]*)\"$")
    public void validateVersionAndContext(final String enabledFlag, final String context) {
        Assertions.assertNotNull(restApiAnnotations);
        Assertions.assertTrue(restApiAnnotations.length > 0);
        String transformedEnabledFlag = "";
        String contextString = "";
        for (RestApiEndpoint annotation : restApiAnnotations) {
            EndpointContextContainer contextContainer = transformer.getEndpointContext(annotation);
            transformedEnabledFlag = String.join(",", transformedEnabledFlag, String.valueOf(contextContainer.isEnabled()));
            contextString = String.join(",", contextString, contextContainer.getEndpointContext());
        }
        //Pull off the first , from the join on empty
        final String actualEnabledFlag = transformedEnabledFlag.substring(1);
        final String actualContext = contextString.substring(1);
        Assertions.assertAll("annotation",
            () -> Assertions.assertEquals(enabledFlag, actualEnabledFlag),
            () -> Assertions.assertEquals(context, actualContext));
    }
}
