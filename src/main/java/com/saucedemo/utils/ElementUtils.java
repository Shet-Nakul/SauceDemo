package com.saucedemo.utils;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ElementUtils - Reusable, cross-cutting element interaction helpers.
 * Used by Page Object classes to avoid direct driver interaction duplication.
 */
public class ElementUtils {

    private static final Logger log = LoggerFactory.getLogger(ElementUtils.class);
    private final WebDriver driver;
    private final WaitUtils waitUtils;

    public ElementUtils(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    /** Click on an element identified by locator (waits for clickability). */
    @Step("Click element: {locator}")
    public void click(By locator) {
        WebElement element = waitUtils.waitForClickability(locator);
        element.click();
        log.debug("Clicked: {}", locator);
    }

    /** Clear and type text into an input field. */
    @Step("Enter text '{text}' into: {locator}")
    public void enterText(By locator, String text) {
        WebElement element = waitUtils.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
        log.debug("Entered '{}' into: {}", text, locator);
    }

    /** Get trimmed visible text of an element. */
    public String getText(By locator) {
        return waitUtils.waitForVisibility(locator).getText().trim();
    }

    /** Check if element is displayed (returns false instead of throwing). */
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /** Check if element is enabled. */
    public boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /** Get attribute value of an element. */
    public String getAttribute(By locator, String attribute) {
        return waitUtils.waitForPresence(locator).getAttribute(attribute);
    }

    /** Select dropdown option by visible text. */
    public void selectByVisibleText(By locator, String visibleText) {
        Select select = new Select(waitUtils.waitForVisibility(locator));
        select.selectByVisibleText(visibleText);
        log.debug("Selected '{}' from dropdown: {}", visibleText, locator);
    }

    /** Get all text values from a list of elements. */
    public List<String> getTextList(By locator) {
        return waitUtils.waitForAllVisible(locator)
                .stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
    }

    /** Get count of elements matching locator. */
    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    /** Scroll to element using JavaScript. */
    public void scrollToElement(By locator) {
        WebElement element = waitUtils.waitForPresence(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /** Click element using JavaScript executor (fallback). */
    public void jsClick(By locator) {
        WebElement element = waitUtils.waitForPresence(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        log.debug("JS-clicked: {}", locator);
    }

    /** Hover over element using Actions. */
    public void hoverOver(By locator) {
        WebElement element = waitUtils.waitForVisibility(locator);
        new Actions(driver).moveToElement(element).perform();
    }

    /** Navigate to a URL. */
    public void navigateTo(String url) {
        driver.get(url);
        log.info("Navigated to: {}", url);
    }

    /** Get current page URL. */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /** Get current page title. */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /** Refresh the current page. */
    public void refreshPage() {
        driver.navigate().refresh();
    }
}
