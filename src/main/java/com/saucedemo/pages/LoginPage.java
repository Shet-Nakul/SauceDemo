package com.saucedemo.pages;

import com.saucedemo.utils.ElementUtils;
import com.saucedemo.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage - Page Object Model for https://www.saucedemo.com/ (login screen).
 * All locators are defined using By.xpath.
 */
public class LoginPage {

    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitUtils waitUtils;

    // ──────────────────────────────────────────────
    //  Locators
    // ──────────────────────────────────────────────
    private static final By USERNAME_INPUT =By.xpath("//input[@data-test='user']");

    private static final By PASSWORD_INPUT =By.xpath("//input[@data-test='pass']");

    private static final By LOGIN_BUTTON =By.xpath("//input[@data-test='login-button']");

    private static final By ERROR_MESSAGE =By.xpath("//h3[@data-test='error']");

    private static final By ERROR_CLOSE_BUTTON =By.xpath("//button[@class='error-button']");

    private static final By LOGIN_LOGO =By.xpath("//div[@class='login_logo']");

    private static final By LOGIN_CREDENTIALS_BOX =By.xpath("//div[@class='login_credentials_wrap-inner']");

    private static final By ACCEPTED_USERNAMES_HEADER =By.xpath("//div[@class='login_credentials']/h4");

    // ──────────────────────────────────────────────
    //  Constructor
    // ──────────────────────────────────────────────
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        this.waitUtils = new WaitUtils(driver);
    }

    // ──────────────────────────────────────────────
    //  Page Actions
    // ──────────────────────────────────────────────

    /** Open the SauceDemo login page. */
    @Step("Open SauceDemo login page")
    public LoginPage open(String baseUrl) {
        elementUtils.navigateTo(baseUrl);
        waitUtils.waitForVisibility(LOGIN_BUTTON);
        return this;
    }

    /** Enter username into the username field. */
    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        elementUtils.enterText(USERNAME_INPUT, username);
        return this;
    }

    /** Enter password into the password field. */
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        elementUtils.enterText(PASSWORD_INPUT, password);
        return this;
    }

    /** Click the Login button. */
    @Step("Click Login button")
    public void clickLoginButton() {
        elementUtils.click(LOGIN_BUTTON);
    }

    /**
     * Full login flow: enter credentials and click login.
     * Returns InventoryPage on success.
     */
    @Step("Login with username: {username}")
    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new InventoryPage(driver);
    }

    /**
     * Attempt login that is expected to fail (wrong credentials / locked user).
     * Stays on LoginPage.
     */
    @Step("Attempt login with username: {username} (expected to fail)")
    public LoginPage loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return this;
    }

    /** Click the X button to dismiss the error banner. */
    @Step("Close error message")
    public LoginPage closeErrorMessage() {
        elementUtils.click(ERROR_CLOSE_BUTTON);
        return this;
    }

    // ──────────────────────────────────────────────
    //  Getters / Assertions helpers
    // ──────────────────────────────────────────────

    /** Returns the error message text displayed on failed login. */
    public String getErrorMessage() {
        return elementUtils.getText(ERROR_MESSAGE);
    }

    /** Returns true if the error message container is visible. */
    public boolean isErrorDisplayed() {
        return elementUtils.isDisplayed(ERROR_MESSAGE);
    }

    /** Returns true if the Login button is displayed. */
    public boolean isLoginButtonDisplayed() {
        return elementUtils.isDisplayed(LOGIN_BUTTON);
    }

    /** Returns true if we are still on the login page (Login button visible). */
    public boolean isOnLoginPage() {
        return elementUtils.isDisplayed(LOGIN_BUTTON);
    }

    /** Returns the logo text. */
    public String getLogoText() {
        return elementUtils.getText(LOGIN_LOGO);
    }

    /** Returns the current value of the username input field. */
    public String getUsernameFieldValue() {
        return elementUtils.getAttribute(USERNAME_INPUT, "value");
    }

    /** Returns the current value of the password input field. */
    public String getPasswordFieldValue() {
        return elementUtils.getAttribute(PASSWORD_INPUT, "value");
    }

    /** Returns the page title. */
    public String getTitle() {
        return elementUtils.getPageTitle();
    }

    /** Returns the accepted usernames section header text. */
    public String getAcceptedUsernamesHeader() {
        return elementUtils.getText(ACCEPTED_USERNAMES_HEADER);
    }

    /** Clears the username input field. */
    @Step("Clear username field")
    public LoginPage clearUsernameField() {
        driver.findElement(USERNAME_INPUT).clear();
        return this;
    }

    /** Clears the password input field. */
    @Step("Clear password field")
    public LoginPage clearPasswordField() {
        driver.findElement(PASSWORD_INPUT).clear();
        return this;
    }
}
