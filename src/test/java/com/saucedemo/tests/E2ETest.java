package com.saucedemo.tests;

import com.saucedemo.pages.*;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * E2ETest - End-to-end tests and miscellaneous/cross-page test scenarios.
 * TC_E2E_01 to TC_E2E_05
 */
@Epic("SauceDemo E2E Tests")
@Feature("End-to-End Flows")
public class E2ETest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndReset() {
        inventoryPage = openAndLogin();
        inventoryPage.waitForPageLoad();
        inventoryPage.resetAppState();
    }

    // ──────────────────────────────────────────────
    //  TC_E2E_01 - Full E2E: login → add item → checkout → confirm
    // ──────────────────────────────────────────────
    @Test(priority = 1, description = "Full E2E: login, add product, checkout, and verify confirmation")
    @Story("Full Purchase Flow")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Simulates a real user completing a full purchase from login to order confirmation.")
    public void testFullPurchaseFlow() {
        // Add product
        inventoryPage.addToCartByName("Sauce Labs Onesie");
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Cart should have 1 item");

        // Navigate to cart
        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Onesie"), "Item should be in cart");

        // Checkout
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.waitForStep1Load();
        checkoutPage.fillCheckoutInfo("Test", "User", "10001");
        checkoutPage.clickContinue();
        checkoutPage.waitForStep2Load();
        checkoutPage.clickFinish();

        // Confirm
        Assert.assertEquals(checkoutPage.getCompleteHeaderText(), "Thank you for your order!",
                "Order completion header mismatch");
        Assert.assertTrue(checkoutPage.getCurrentUrl().contains("checkout-complete"),
                "URL should contain checkout-complete");
    }

    // ──────────────────────────────────────────────
    //  TC_E2E_02 - Add from detail page → checkout
    // ──────────────────────────────────────────────
    @Test(priority = 2, description = "Add to cart from product detail page and complete checkout")
    @Story("Checkout from Detail Page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("User adds product from the detail page and completes the checkout flow.")
    public void testAddFromDetailAndCheckout() {
        // Go to product detail
        ProductDetailPage detailPage = inventoryPage.clickOnProductName("Sauce Labs Bolt T-Shirt");
        detailPage.waitForPageLoad();

        // Add to cart from detail page
        detailPage.addToCart();
        Assert.assertEquals(detailPage.getCartBadgeCount(), 1, "Cart badge should show 1");

        // Navigate to cart → checkout
        CartPage cartPage = detailPage.goToCart();
        cartPage.waitForPageLoad();
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");

        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.waitForStep1Load();
        checkoutPage.fillCheckoutInfo("Sam", "Lee", "99501");
        checkoutPage.clickContinue();
        checkoutPage.waitForStep2Load();
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.getCurrentUrl().contains("checkout-complete"),
                "Should land on checkout-complete page");
    }

    // ──────────────────────────────────────────────
    //  TC_E2E_03 - Continue shopping from cart returns to inventory
    // ──────────────────────────────────────────────
    @Test(priority = 3, description = "Verify Continue Shopping button from cart returns to inventory")
    @Story("Cart Navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clicking Continue Shopping from the Cart page should return user to the inventory.")
    public void testContinueShoppingFromCart() {
        inventoryPage.addToCartByName("Sauce Labs Backpack");

        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();

        InventoryPage backToInventory = cartPage.clickContinueShopping();
        backToInventory.waitForPageLoad();

        Assert.assertTrue(backToInventory.getCurrentUrl().contains("inventory"),
                "URL should contain 'inventory' after Continue Shopping");
        Assert.assertEquals(backToInventory.getPageTitle(), "Products",
                "Should be back on Products page");
        Assert.assertEquals(backToInventory.getCartBadgeCount(), 1,
                "Cart should still have 1 item after continuing shopping");
    }

    // ──────────────────────────────────────────────
    //  TC_E2E_04 - Sort high to low and verify first is most expensive
    // ──────────────────────────────────────────────
    @Test(priority = 4, description = "Verify sort by price high-to-low places most expensive product first")
    @Story("Product Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Sorting by 'Price (high to low)' should place the most expensive item at index 0.")
    public void testSortHighToLow() {
        inventoryPage.sortProductsBy("Price (high to low)");

        List<Double> prices = inventoryPage.getAllProductPricesAsDouble();
        Assert.assertTrue(prices.size() > 0, "Price list should not be empty");

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i + 1),
                    String.format("Prices not in descending order at index %d: %.2f < %.2f",
                            i, prices.get(i), prices.get(i + 1)));
        }
    }

    // ──────────────────────────────────────────────
    //  TC_E2E_05 - Product detail page: price, name, description are non-empty
    // ──────────────────────────────────────────────
    @Test(priority = 5, description = "Verify product detail page shows non-empty name, description, and price")
    @Story("Product Detail Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("All key product info fields should be populated on the product detail page.")
    public void testProductDetailPageContent() {
        ProductDetailPage detailPage = inventoryPage.clickOnProductName("Sauce Labs Fleece Jacket");
        detailPage.waitForPageLoad();

        String name = detailPage.getProductName();
        String description = detailPage.getProductDescription();
        String price = detailPage.getProductPrice();

        Assert.assertFalse(name.isEmpty(), "Product name should not be empty");
        Assert.assertFalse(description.isEmpty(), "Product description should not be empty");
        Assert.assertFalse(price.isEmpty(), "Product price should not be empty");
        Assert.assertTrue(price.startsWith("$"), "Product price should start with '$'");
        Assert.assertTrue(detailPage.getProductPriceAsDouble() > 0,
                "Product price should be greater than 0");
    }
}
