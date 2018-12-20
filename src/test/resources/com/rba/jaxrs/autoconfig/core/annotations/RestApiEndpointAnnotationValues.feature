Feature: Are Rest Api Endpoint Annotation Values passed through?
  Everyone wants to know that the values passed into the annotation can be retrieved.

  Scenario Outline: Rest Api Annotation did or did not retain values
    Given stub class "<className>" with annotation
    When I inspect the class annotations
    Then I should be told "<version>" and "<context>"

    Examples:
    | className | version | context |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithEmptyAnnotation | EMPTY_API_VERSION | EMPTY_API_CONTEXT |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithVersionAndMultipleContexts | EXTERNAL_V2 | OPEN/ADMIN/TEST |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithVersionAndSingleContext | EXTERNAL_V1 | OPEN |
    | com.rba.jaxrs.autoconfig.stubs.StubEndpointWithMultipleAnnotations | EXTERNAL_V1,INTERNAL | OPEN,OPEN/TEST |
