# Risk Narrative Automation Exercise

This project tests the Selenium Dev UI and the Glax Weather API.


### Pre-requisites
- Java 17
- IDE - preferably IntelliJ
- Intellij IDE Plugins - Cucumber, Gherkin, Lombok

#### API Tests
- To generate an API Key for running the Met office API tests, follow the instructions found here:
  `https://www.metoffice.gov.uk/services/data/datapoint/getting-started`
- In order to get API test to work, you will need to update the `APIKeys.properties` with the generated API key.

#### UI Tests
- Make sure you have the latest version of Chrome installed on your machine.
- The drivers are automatically downloaded by serenity.
- To find out what version of Chrome you have installed, go to `chrome://settings/help` in your Chrome browser.


### Running tests from terminal
- Make sure the `cucumber.filter.tags` property in the `cucumber.properties` file has the value `@API and @UI`. `cucumber.filter.tags=@API and @UI`
- Then run the command `mvn clean verify` in the terminal

#### Running tests by tags
- To run only API tests use `mvn clean verify -Dcucumber.filter.tags='@API'`
- To run only UI tests use `mvn clean verify -Dcucumber.filter.tags='@UI'`
- To run both API and UI tests use `mvn clean verify -Dcucumber.filter.tags='@API and @UI'` or just `mvn clean verify`, this also allows you to run all tests.

### Running tests from IDE
- To run either the API or UI tests from the runner class, right-click on the `src/test/java/com/risk/narrative/runners/<test type>TestRunner.java` file and click on `Run '<test type>TestRunner'`. You may need to modify the run configuration to make sure, the glue is pointing to the stepDefinitions folder.
- Alternatively, you can right-click on the individual feature file in the `src/test/resources/features/<test type>` folder and click on `Run '<feature file>'`
- Or press the play button next to the scenarios in the feature file to run individual scenarios.

### Viewing test reports
- After running the tests using the `mvn` command you prefer, the Serenity test reports are generated, this includes the FULL reports and the Single Page HTML Summary.
- A link is provided to access both reports at the end of the results. Where the results are provided, you will also see a breakdown of the tests that were ran. 
- A copy of the Full report can be found in the `target/site/serenity/index.html` folder.
- A copy of the Single Page HTML Summary report can be found in the `target/site/serenity/serenity-summary.html` folder.
- Cucumber reports are generated and can be found here `target/cucumber-report.html`.

