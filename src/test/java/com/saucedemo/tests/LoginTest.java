package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest - Test cases covering the login functionality of SauceDemo.
 * TC_LOGIN_01 to TC_LOGIN_05
 */
@Epic("SauceDemo E2E Tests")
@Feature("Login")
public class LoginTest extends BaseTest {

    // ──────────────────────────────────────────────
    //  TC_LOGIN_01 - Valid login
    // ──────────────────────────────────────────────
    @Test(priority = 1, description = "Verify successful login with valid credentials")
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("User should be redirected to the inventory/products page after a successful login.")
    public void testValidLogin() {
        InventoryPage inventoryPage = openAndLogin();
        inventoryPage.waitForPageLoad();

        Assert.assertTrue(inventoryPage.getCurrentUrl().contains("inventory"),
                "URL should contain 'inventory' after successful login");
        Assert.assertEquals(inventoryPage.getPageTitle(), "Products",
                "Page title should be 'Products'");
        Assert.assertTrue(inventoryPage.isInventoryListDisplayed(),
                "Inventory list should be visible after login");
    }

    // ──────────────────────────────────────────────
    //  TC_LOGIN_02 - Invalid password
    // ──────────────────────────────────────────────
    @Test(priority = 2, description = "Verify error message displayed for invalid password")
    @Story("Invalid Login - Wrong Password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("User should see an error message when password is incorrect.")
    public void testInvalidPasswordLogin() {
        LoginPage login = openLoginPage();
        login.loginExpectingFailure(config.getValidUsername(), config.getInvalidPassword());

        Assert.assertTrue(login.isErrorDisplayed(),
                "Error message should be displayed for invalid password");
        Assert.assertTrue(login.getErrorMessage().contains("Username and password do not match"),
                "Error message text mismatch. Actual: " + login.getErrorMessage());
        Assert.assertTrue(login.isOnLoginPage(),
                "User should remain on the login page");
    }

    // ──────────────────────────────────────────────
    //  TC_LOGIN_03 - Locked out user
    // ──────────────────────────────────────────────
    @Test(priority = 3, description = "Verify locked user cannot login")
    @Story("Invalid Login - Locked User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The locked_out_user account should display a locked error message.")
    public void testLockedUserLogin() {
        LoginPage login = openLoginPage();
        login.loginExpectingFailure(config.getLockedUsername(), config.getValidPassword());

        Assert.assertTrue(login.isErrorDisplayed(),
                "Error message should be displayed for locked user");
        Assert.assertTrue(login.getErrorMessage().contains("Sorry, this user has been locked out"),
                "Error message should indicate account is locked. Actual: " + login.getErrorMessage());
    }

    // ──────────────────────────────────────────────
    //  TC_LOGIN_04 - Empty credentials
    // ──────────────────────────────────────────────
    @Test(priority = 4, description = "Verify error when submitting empty username and password")
    @Story("Invalid Login - Empty Fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("User should see a validation error when both fields are empty.")
    public void testEmptyCredentialsLogin() {
        LoginPage login = openLoginPage();
        login.loginExpectingFailure("", "");

        Assert.assertTrue(login.isErrorDisplayed(),
                "Error message should appear for empty credentials");
        Assert.assertTrue(login.getErrorMessage().toLowerCase().contains("username is required"),
                "Error should mention username is required. Actual: " + login.getErrorMessage());
    }

    // ──────────────────────────────────────────────
    //  TC_LOGIN_05 - Logout and verify redirect
    // ──────────────────────────────────────────────
    @Test(priority = 5, description = "Verify successful logout redirects to login page")
    @Story("Logout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After logging out, user should be redirected back to the login page.")
    public void testLogout() {
        InventoryPage inventoryPage = openAndLogin();
        inventoryPage.waitForPageLoad();

        LoginPage login = inventoryPage.logout();

        Assert.assertTrue(login.isLoginButtonDisplayed(),
                "Login button should be visible after logout");
        Assert.assertTrue(login.isOnLoginPage(),
                "User should be on the login page after logout");
        Assert.assertTrue(driver.getCurrentUrl().equals(config.getBaseUrl()) ||
                          driver.getCurrentUrl().contains("saucedemo.com"),
                "URL should point back to SauceDemo after logout");
    }
}
