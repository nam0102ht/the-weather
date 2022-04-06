Feature: the weather blackbox test

  Scenario Outline: Success Scenarios
    Given Send Request with key "<Request>" message to API
    When Service receive message from API
    Then Response must be "<Response>"
    Examples:
      |   Request             |   Response              |
      |   London              |   response_LonDon.json  |