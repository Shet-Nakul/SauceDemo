package com.saucedemo.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtils - Captures and attaches screenshots to Allure reports.
 */
public class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "target/screenshots/";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {}

    /**
     * Captures a screenshot and attaches it to the current Allure step.
     *
     * @param driver      WebDriver instance
     * @param stepName    Descriptive name shown in Allure
     */
    public static void captureAndAttach(WebDriver driver, String stepName) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(stepName, new ByteArrayInputStream(screenshot));
            log.info("Screenshot captured and attached: {}", stepName);
        } catch (Exception e) {
            log.warn("Failed to capture screenshot for step '{}': {}", stepName, e.getMessage());
        }
    }

    /**
     * Captures a screenshot and saves it to disk under target/screenshots/.
     *
     * @param driver    WebDriver instance
     * @param testName  Test name used as file prefix
     * @return Path to saved file, or null on failure
     */
    public static Path captureToFile(WebDriver driver, String testName) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String fileName = testName.replaceAll("[^a-zA-Z0-9_]", "_") + "_" + timestamp + ".png";

            Path screenshotDir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(screenshotDir);

            Path destination = screenshotDir.resolve(fileName);
            Files.copy(srcFile.toPath(), destination);
            log.info("Screenshot saved: {}", destination.toAbsolutePath());
            return destination;

        } catch (IOException e) {
            log.warn("Failed to save screenshot for '{}': {}", testName, e.getMessage());
            return null;
        }
    }

    /**
     * Captures a screenshot on test failure and attaches to Allure.
     * Convenience method combining disk-save + Allure attachment.
     */
    public static void captureOnFailure(WebDriver driver, String testName) {
        captureAndAttach(driver, "FAILURE - " + testName);
        captureToFile(driver, "FAIL_" + testName);
    }
}
