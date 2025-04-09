@API
Feature: Validate Glax Weather API works as expected

  Scenario: Verify retrieval of Weather Details
    Given I send a GET request to the Data Dragons API
    Then I verify the status code is 200
    And I verify the following headers are present in the response
      | temperature   |
      | humidity      |
      | weather       |
      | icon          |
      | city          |
      | message       |
      | credits       |
      | timestamp_utc |
      | wind_speed    |
    And I verify the following fields have the expected values
      | speed_unit       | km/h |
      | temperature_unit | C    |

