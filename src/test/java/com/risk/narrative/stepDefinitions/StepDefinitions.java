package com.risk.narrative.stepDefinitions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.risk.narrative.api.models.GlaxWeatherResponse;
import com.risk.narrative.ui.pages.WebFormPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.util.EnvironmentVariables;

@Slf4j
public class StepDefinitions {

  @Steps
  WebFormPage webFormPage;


//  UI Steps
  @Given("I load up the Selenium Dev website")
  public void iLoadUpTheSeleniumDevWebsite() {
    webFormPage.open();
  }

  @And("I fill in the following fields:")
  @And("I fill in the following dropdown fields:")
  public void iFillInTheFollowingFields(DataTable dataTable) {
    List<Map<String, String>> fields = dataTable.asMaps();
    for (Map<String, String> row : fields) {
      webFormPage.populateInputField(row.get("field"), row.get("value"));
    }
  }

  @And("I verify the following input fields are present")
  public void iVerifyTheFollowingInputFieldsArePresent(List<String> fieldNames) {
    for(String fieldName: fieldNames) {
      webFormPage.validateInputField(fieldName);
    }
  }

  @And("I select the following options:")
  public void iSelectTheFollowingOptions(DataTable dataTable) {
    List<Map<String, String>> options = dataTable.asMaps();
    for (Map<String, String> row : options) {
      webFormPage.clickCheckboxOrRadio(row.get("option"), row.get("type"));
    }
  }

  @Then("I select today's date from the Date picker")
  public void iSelectTodaySFromTheDatePicker() {
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")); // 04-09-2025
    webFormPage.populateInputField("Date", today);
  }

  @And("I click {string}")
  public void iClick(String button) {
    webFormPage.clickButton(button);
  }

  @And("I select a random colour from the Color picker")
  public void iSelectARandomColourFromTheColorPicker() {
    String randomColour = webFormPage.generateRandomHexNumber();
    webFormPage.populateInputField("color", randomColour);
  }

  @Then("I see a confirmation page with the following text attributes")
  public void iSeeAConfirmationPageWithTheFollowingTextAttributes(DataTable dataTable) {
    List<Map<String, String>> attributes = dataTable.asMaps();
    for (Map<String, String> row : attributes) {
      webFormPage.validateText(row.get("attribute"), row.get("text"));
    }
  }


//  API Steps
  private Response response;
  private EnvironmentVariables environmentVariables;

  public GlaxWeatherResponse glaxWeatherResponse() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper.readValue(response.getBody().asString(), GlaxWeatherResponse.class);
  }



  public Response sendRequestToApi() {
    String baseUrl = environmentVariables.getProperty("restapi.baseurl");
    return SerenityRest.given().when().baseUri(baseUrl)
        .queryParam("location", "")
        .queryParam("lon", "46.9481")
        .queryParam("lat", "7.4474")
        .queryParam("units", "metric")
        .get();
  }

  @Given("I send a GET request to the Data Dragons API")
  public void iSendAGETRequestToTheDataDragonsAPI() {
    response = sendRequestToApi();
    log.info(response.getBody().prettyPrint());
  }

  @Then("I verify the status code is {int}")
  public void iVerifyTheStatusCodeIs(int statusCode) {
    assertEquals(response.statusCode(), statusCode);
  }

  @And("I verify the following headers are present in the response")
  public void iVerifyTheFollowingHeadersArePresentInTheResponse(List<String> headerNames)
      throws JsonProcessingException {
    for (String headerName : headerNames) {
      try {
        Field field = GlaxWeatherResponse.class.getDeclaredField(headerName);
        field.setAccessible(true);
        Object value = field.get(glaxWeatherResponse());

        assertThat(value)
            .as("Expected header '" + headerName + "' to be present in the response and not null")
            .isNotNull();
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new AssertionError("Header '" + headerName + "' is missing in the POJO or inaccessible", e);
      }
    }
  }

  @And("I verify the following fields have the expected values")
  public void iVerifyTheFollowingFieldsHaveTheExpectedValues(DataTable dataTable)
      throws JsonProcessingException {
    Map<String, String> dataItems = dataTable.asMap(String.class, String.class);

    for(Map.Entry<String, String> entry : dataItems.entrySet()){
      String header = entry.getKey();
      String expectedheaderValue = entry.getValue();

      try{
        Field field = GlaxWeatherResponse.class.getDeclaredField(header);
        field.setAccessible(true);
        Object value = field.get(glaxWeatherResponse());

        assertEquals(value, expectedheaderValue);
        log.info(header);
        log.info(value.toString());
        log.info(expectedheaderValue);

      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
