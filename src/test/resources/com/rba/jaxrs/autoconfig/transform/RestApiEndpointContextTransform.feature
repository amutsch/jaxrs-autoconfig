Feature: Are Rest Api Endpoint Annotation Values properly transformed?
  Everyone wants to know that the annotation values are properly transformed to an actual address context.

  Scenario Outline: Rest Api Annotation is resolved to a proper address context.
    Given stub class "<className>" with annotation
    When I inspect the class annotations
    Then I should be told "<enabledFlag>" and "<context>"

    Examples:
    | className | enabledFlag | context |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithEmptyAnnotation | true | / |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithVersionAndMultipleContexts | false | /v2/open/admin/testing |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithVersionAndSingleContext | true | /v1/open |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithMultipleAnnotations | true,false | /v1/open,/open/testing |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointEmptyValuesInAnnotation | true | / |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithNullContexts | true | / |
