package com.saucedemo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverManager - Thread-safe WebDriver lifecycle management using ThreadLocal.
 * Supports Chrome, Firefox, and Edge browsers with optional headless mode.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private DriverManager() {
        // Prevent instantiation
    }

    /**
     * Initializes and returns a WebDriver instance for the current thread.
     */
    public static WebDriver initDriver() {
        String browser = config.getBrowser().toLowerCase().trim();
        boolean headless = config.isHeadless();
        WebDriver driver;

        switch (browser) {

            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }

                driver = new FirefoxDriver(firefoxOptions);
            }

            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();

                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }

                driver = new EdgeDriver(edgeOptions);
            }

            default -> {
                // Default: Chrome
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();

                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }

                // Stability & performance
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-notifications");

                // 🔥 Disable password manager & breach popup
                chromeOptions.addArguments("--disable-save-password-bubble");
                chromeOptions.addArguments("--disable-features=PasswordLeakDetection");

                // 🔥 Disable credentials service via prefs
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);

                chromeOptions.setExperimentalOption("prefs", prefs);

                // Optional: clean session
                chromeOptions.addArguments("--incognito");

                driver = new ChromeDriver(chromeOptions);
            }
        }

        // Common driver setup
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        driverThreadLocal.set(driver);
        return driver;
    }

    /**
     * Returns the WebDriver instance for the current thread.
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialised. Call initDriver() first.");
        }
        return driver;
    }

    /**
     * Quits the WebDriver and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}