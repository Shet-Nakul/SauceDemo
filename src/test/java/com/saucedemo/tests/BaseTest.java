package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * BaseTest - Parent class for all test classes.
 * Handles WebDriver lifecycle (init/quit), login helpers, and screenshot on failure.
 */
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ConfigReader config;
    protected LoginPage loginPage;

    // ──────────────────────────────────────────────
    //  TestNG lifecycle
    // ──────────────────────────────────────────────

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        config = ConfigReader.getInstance();
        log.info("Initialising WebDriver...");
        driver = DriverManager.initDriver();
        log.info("WebDriver initialised: {}", driver.getClass().getSimpleName());
    }

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        loginPage = new LoginPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void handleTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test FAILED: {}", result.getName());
            ScreenshotUtils.captureOnFailure(driver, result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("Test PASSED: {}", result.getName());
        } else {
            log.warn("Test SKIPPED: {}", result.getName());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        log.info("Quitting WebDriver...");
        DriverManager.quitDriver();
    }

    // ──────────────────────────────────────────────
    //  Shared helper methods
    // ──────────────────────────────────────────────

    /**
     * Opens the login page and performs a standard login with valid credentials.
     * Returns the InventoryPage after successful login.
     */
    @Step("Open app and login with valid credentials")
    protected InventoryPage openAndLogin() {
        loginPage.open(config.getBaseUrl());
        return loginPage.login(config.getValidUsername(), config.getValidPassword());
    }

    /**
     * Opens the login page without logging in.
     */
    @Step("Open login page")
    protected LoginPage openLoginPage() {
        return loginPage.open(config.getBaseUrl());
    }

    /**
     * Adds a label to the current Allure test.
     */
    protected void addAllureLabel(String name, String value) {
        Allure.label(name, value);
    }
}
