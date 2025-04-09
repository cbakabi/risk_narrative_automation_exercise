package com.risk.narrative.ui.pages;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Random;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.steps.UIInteractions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class WebFormPage extends UIInteractions {

  private WebElementFacade findFieldElement(String field) {
    String name = field.toLowerCase();

    return switch (name) {
      case "text", "password", "file", "color" ->
          find(By.cssSelector("input[type='" + field + "']"));
      case "submit" -> find(By.cssSelector("button[type='" + field + "']"));
      case "textarea" -> find(By.cssSelector("textarea[name*='textarea']"));
      case "disabled input" -> find(By.cssSelector("input[placeholder='" + field + "']"));
      case "readonly input" -> find(By.cssSelector("input[value='" + field + "']"));
      case "select", "datalist", "date" -> find(By.name("my-" + name));
      case "message" -> find(By.id("message"));
      default -> throw new IllegalArgumentException("Unrecognized field name: " + field);
    };
  }

  //  Input field interactions
  @Step("Populate field: {0} with value: {1}")
  public void populateInputField(String field, String value) {
    if ("color".equalsIgnoreCase(field)) {
      value = generateRandomHexNumber();
    }

    WebElementFacade element = findFieldElement(field);
    element.waitUntilVisible();

    switch (element.getTagName().toLowerCase()) {
      case "select" -> element.selectByVisibleText(value);
      case "textarea", "input" -> {
        element.clear();
        element.sendKeys(value);

        if ("datalist".equalsIgnoreCase(field) || "date".equalsIgnoreCase(field)
            || "color".equalsIgnoreCase(field)) {
          element.sendKeys(Keys.TAB);
        }
      }
      default -> throw new UnsupportedOperationException(
          "Unsupported element type: " + element.getTagName());
    }
  }

  @Step("Validate field by type: {0}")
  public void validateInputField(String field) {
    WebElementFacade element = findFieldElement(field);

    switch (field.toLowerCase()) {
      case "disabled input" -> element.isDisabled();
      case "readonly input" -> element.shouldBeVisible();
      case "file" -> element.isClickable();
      default -> throw new IllegalArgumentException("Unrecognized field name: " + field);
    }
  }

  public String generateRandomHexNumber() {
    Random rand = new Random();
    int r = rand.nextInt(256);
    int g = rand.nextInt(256);
    int b = rand.nextInt(256);
    return String.format("#%02X%02X%02X", r, g, b); // e.g., #ff0000
  }

  //Checkbox and Radio buttons

  public WebElementFacade findCheckBoxOrRadioButton(String option, String type) {
    String suffix = switch (option.toLowerCase()) {
      case "checked" -> "1";
      case "default" -> "2";
      default -> throw new IllegalArgumentException("Unknown state: " + option);
    };

    String idPrefix = switch (type.toLowerCase()) {
      case "checkbox" -> "my-check-";
      case "radio" -> "my-radio-";
      default -> throw new IllegalArgumentException("Unknown type: " + type);
    };

    return find(By.id(idPrefix + suffix));
  }

  @Step("Click checkbox or radio")
  public void clickCheckboxOrRadio(String checkboxName, String option) {
    WebElementFacade optionSelection = findCheckBoxOrRadioButton(checkboxName, option);

    optionSelection.waitUntilVisible().click();
  }

  @Step("Click button")
  public void clickButton(String buttonName) {
    WebElementFacade button = findFieldElement(buttonName.toLowerCase());

    button.waitUntilVisible().click();
  }

  @Step("Validate Text")
  public void validateText(String attribute, String text) {
    WebElementFacade textType = resolveElementFor(attribute, text);

    textType.waitUntilVisible().shouldContainText(text);

    assertThat(textType.isVisible()).as(attribute + " should be visible").isTrue();
  }

  private WebElementFacade resolveElementFor(String attribute, String expectedText) {
    switch (attribute.toLowerCase()) {
      case "header" -> {
        return find(By.xpath("//h1[contains(normalize-space(text()), '" + expectedText + "')]"));
      }
      case "message" -> {
        return findFieldElement(attribute);
      }
      default -> throw new IllegalArgumentException("Unable to resolve element");
    }
  }
}
