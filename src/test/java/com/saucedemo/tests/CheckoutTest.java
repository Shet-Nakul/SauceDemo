package com.saucedemo.tests;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.InventoryPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * CheckoutTest - Test cases covering the end-to-end checkout flow.
 * TC_CHK_01 to TC_CHK_05
 */
@Epic("SauceDemo E2E Tests")
@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    private InventoryPage inventoryPage;

    private static final String PRODUCT = "Sauce Labs Fleece Jacket";

    @BeforeMethod(alwaysRun = true)
    public void loginAndAddProduct() {
        inventoryPage = openAndLogin();
        inventoryPage.waitForPageLoad();
        inventoryPage.resetAppState();
        inventoryPage.addToCartByName(PRODUCT);
    }

    // ──────────────────────────────────────────────
    //  TC_CHK_01 - Complete checkout successfully
    // ──────────────────────────────────────────────
    @Test(priority = 1, description = "Verify a complete checkout flow from cart to order confirmation")
    @Story("Complete Checkout")
    @Severity(SeverityLevel.BLOCKER)
    @Description("User should be able to complete a purchase and see the order confirmation page.")
    public void testCompleteCheckoutFlow() {
        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();

        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.waitForStep1Load();
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutPage.waitForStep2Load();
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.getCurrentUrl().contains("checkout-complete"),
                "URL should contain 'checkout-complete'");
        Assert.assertEquals(checkoutPage.getCompleteHeaderText(), "Thank you for your order!",
                "Order confirmation header mismatch");
        Assert.assertTrue(checkoutPage.isOrderCompleteImageDisplayed(),
                "Order complete image should be displayed");
    }

    // ──────────────────────────────────────────────
    //  TC_CHK_02 - Checkout step 1: missing first name
    // ──────────────────────────────────────────────
    @Test(priority = 2, description = "Verify error when first name is omitted on checkout step 1")
    @Story("Checkout Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Submitting checkout step 1 without a first name should show a validation error.")
    public void testCheckoutMissingFirstName() {
        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();

        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.waitForStep1Load();
        checkoutPage.fillCheckoutInfo("", "Doe", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isErrorDisplayed(),
                "Error message should be shown when first name is missing");
        Assert.assertTrue(checkoutPage.getErrorMessage().contains("First Name is required"),
                "Error should mention first name. Actual: " + checkoutPage.getErrorMessage());
    }

    // ──────────────────────────────────────────────
    //  TC_CHK_03 - Checkout step 1: missing postal code
    // ──────────────────────────────────────────────
    @Test(priority = 3, description = "Verify error when postal code is omitted on checkout step 1")
    @Story("Checkout Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Submitting checkout step 1 without a postal code should show a validation error.")
    public void testCheckoutMissingPostalCode() {
        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();

        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.waitForStep1Load();
        checkoutPage.fillCheckoutInfo("Jane", "Smith", "");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isErrorDisplayed(),
                "Error message should be shown when postal code is missing");
        Assert.assertTrue(checkoutPage.getErrorMessage().contains("Postal Code is required"),
                "Error should mention postal code. Actual: " + checkoutPage.getErrorMessage());
    }

    // ──────────────────────────────────────────────
    //  TC_CHK_04 - Verify checkout overview shows correct product and total
    // ──────────────────────────────────────────────
    @Test(priority = 4, description = "Verify checkout overview page shows correct product and price totals")
    @Story("Checkout Overview")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The checkout overview should list the product added to cart and display a valid total.")
    public void testCheckoutOverviewContent() {
        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();

        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.waitForStep1Load();
        checkoutPage.fillCheckoutInfo("Alice", "Brown", "90210");
        checkoutPage.clickContinue();
        checkoutPage.waitForStep2Load();

        Assert.assertTrue(checkoutPage.getOverviewItemNames().contains(PRODUCT),
                "Overview should list the product: " + PRODUCT);
        Assert.assertFalse(checkoutPage.getItemTotalText().isEmpty(),
                "Item total label should not be empty");
        Assert.assertFalse(checkoutPage.getTaxText().isEmpty(),
                "Tax label should not be empty");
        Assert.assertFalse(checkoutPage.getTotalText().isEmpty(),
                "Total label should not be empty");
    }

    // ──────────────────────────────────────────────
    //  TC_CHK_05 - Cancel from checkout overview returns to inventory
    // ──────────────────────────────────────────────
    @Test(priority = 5, description = "Verify clicking Cancel on overview page navigates to inventory")
    @Story("Checkout Navigation")
    @Severity(SeverityLevel.MINOR)
    @Description("Clicking Cancel on step 2 (overview) should return the user to the inventory page.")
    public void testCancelOnCheckoutOverview() {
        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();

        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.waitForStep1Load();
        checkoutPage.fillCheckoutInfo("Bob", "Jones", "54321");
        checkoutPage.clickContinue();
        checkoutPage.waitForStep2Load();

        InventoryPage backToInventory = checkoutPage.clickCancelOnStep2();
        backToInventory.waitForPageLoad();

        Assert.assertTrue(backToInventory.getCurrentUrl().contains("inventory"),
                "URL should contain 'inventory' after cancelling from overview");
        Assert.assertEquals(backToInventory.getPageTitle(), "Products",
                "Page title should be 'Products' after cancel");
    }
}
