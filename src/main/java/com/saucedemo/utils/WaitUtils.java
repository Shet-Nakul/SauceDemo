package com.saucedemo.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * WaitUtils - Centralised explicit wait utility methods.
 * All methods use configurable timeout from ConfigReader.
 */
public class WaitUtils {

    private final WebDriverWait wait;
    private static final long DEFAULT_TIMEOUT = ConfigReader.getInstance().getExplicitWait();

    public WaitUtils(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WaitUtils(WebDriver driver, long timeoutSeconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /** Wait until element is visible and return it. */
    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until element is visible (from WebElement reference). */
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /** Wait until element is clickable and return it. */
    public WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Wait until element is clickable (from WebElement reference). */
    public WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /** Wait until element is present in DOM. */
    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Wait until all elements located by locator are visible. */
    public List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /** Wait until URL contains a given fragment. */
    public boolean waitForUrlContains(String urlFragment) {
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    /** Wait until element is invisible. */
    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Wait until element text contains expected string. */
    public boolean waitForTextInElement(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /** Hard sleep - use sparingly, only where necessary. */
    public static void hardWait(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
