@UI
Feature: Validate Web form page

  Scenario: Verify Web form page works as expected
    Given I load up the Selenium Dev website
    When I fill in the following fields:
      | field    | value              |
      | Text     | hello world!       |
      | Password | TestingP@ssw0rd!   |
      | Textarea | This is a comment. |
    And I verify the following input fields are present
      | Disabled input |
      | Readonly input |
      | File           |
    And I fill in the following dropdown fields:
      | field    | value   |
      | Select   | Two     |
      | Datalist | Chicago |
    And I select the following options:
      | option  | type     |
      | Checked | checkbox |
      | Default | radio    |
    And I select a random colour from the Color picker
    And I select today's date from the Date picker
    And I click "Submit"
    Then I see a confirmation page with the following text attributes
      | attribute | text           |
      | Header    | Form submitted |
      | Message   | Received!      |
