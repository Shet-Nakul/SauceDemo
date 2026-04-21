package com.saucedemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration properties from config.properties file.
 * Singleton pattern ensures a single instance throughout the test run.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    private ConfigReader() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties from: " + CONFIG_FILE_PATH, e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getBrowser() {
        return getProperty("browser");
    }

    public String getValidUsername() {
        return getProperty("valid.username");
    }

    public String getValidPassword() {
        return getProperty("valid.password");
    }

    public String getLockedUsername() {
        return getProperty("locked.username");
    }

    public String getInvalidPassword() {
        return getProperty("invalid.password");
    }

    public long getImplicitWait() {
        return Long.parseLong(getProperty("implicit.wait"));
    }

    public long getExplicitWait() {
        return Long.parseLong(getProperty("explicit.wait"));
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }
}
