package com.saucedemo.tests;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * CartTest - Test cases covering the Shopping Cart functionality.
 * TC_CART_01 to TC_CART_05
 */
@Epic("SauceDemo E2E Tests")
@Feature("Shopping Cart")
public class CartTest extends BaseTest {

    private InventoryPage inventoryPage;

    private static final String PRODUCT_1 = "Sauce Labs Backpack";
    private static final String PRODUCT_2 = "Sauce Labs Bike Light";

    @BeforeMethod(alwaysRun = true)
    public void loginAndResetCart() {
        inventoryPage = openAndLogin();
        inventoryPage.waitForPageLoad();
        // Ensure clean state before each test
        inventoryPage.resetAppState();
    }

    // ──────────────────────────────────────────────
    //  TC_CART_01 - Add single item to cart
    // ──────────────────────────────────────────────
    @Test(priority = 1, description = "Verify adding a single product increments cart badge to 1")
    @Story("Add to Cart")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Adding one product should show '1' on the cart badge.")
    public void testAddSingleItemToCart() {
        inventoryPage.addToCartByName(PRODUCT_1);

        Assert.assertTrue(inventoryPage.isCartBadgeVisible(),
                "Cart badge should be visible after adding an item");
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1,
                "Cart badge should show count of 1");
        Assert.assertTrue(inventoryPage.isProductInCart(PRODUCT_1),
                "Product button should change to 'Remove' after adding to cart");
    }

    // ──────────────────────────────────────────────
    //  TC_CART_02 - Add multiple items to cart
    // ──────────────────────────────────────────────
    @Test(priority = 2, description = "Verify adding multiple products updates cart badge correctly")
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding two products should show '2' on the cart badge.")
    public void testAddMultipleItemsToCart() {
        inventoryPage.addToCartByName(PRODUCT_1);
        inventoryPage.addToCartByName(PRODUCT_2);

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2,
                "Cart badge should show count of 2 after adding two items");
    }

    // ──────────────────────────────────────────────
    //  TC_CART_03 - Remove item from cart on inventory page
    // ──────────────────────────────────────────────
    @Test(priority = 3, description = "Verify removing an item from inventory page decrements cart badge")
    @Story("Remove from Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Removing a product using the 'Remove' button on inventory page should update cart badge.")
    public void testRemoveItemFromInventoryPage() {
        inventoryPage.addToCartByName(PRODUCT_1);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Precondition: cart should have 1 item");

        inventoryPage.removeFromCartByName(PRODUCT_1);

        Assert.assertFalse(inventoryPage.isCartBadgeVisible(),
                "Cart badge should disappear after removing the only item");
        Assert.assertFalse(inventoryPage.isProductInCart(PRODUCT_1),
                "Product button should revert to 'Add to cart'");
    }

    // ──────────────────────────────────────────────
    //  TC_CART_04 - Cart page shows correct items
    // ──────────────────────────────────────────────
    @Test(priority = 4, description = "Verify cart page shows the correct products after adding")
    @Story("Cart Page Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Products added from inventory should be visible on the Cart page with correct names.")
    public void testCartPageShowsCorrectItems() {
        inventoryPage.addToCartByName(PRODUCT_1);
        inventoryPage.addToCartByName(PRODUCT_2);

        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();

        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "Cart should contain 2 items");
        Assert.assertTrue(cartPage.isItemInCart(PRODUCT_1),
                "Cart should contain: " + PRODUCT_1);
        Assert.assertTrue(cartPage.isItemInCart(PRODUCT_2),
                "Cart should contain: " + PRODUCT_2);
    }

    // ──────────────────────────────────────────────
    //  TC_CART_05 - Remove item from cart page
    // ──────────────────────────────────────────────
    @Test(priority = 5, description = "Verify removing an item from the Cart page works correctly")
    @Story("Remove from Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Removing a product from the Cart page should remove it from the list.")
    public void testRemoveItemFromCartPage() {
        inventoryPage.addToCartByName(PRODUCT_1);
        inventoryPage.addToCartByName(PRODUCT_2);

        CartPage cartPage = inventoryPage.clickCartIcon();
        cartPage.waitForPageLoad();

        cartPage.removeItemByName(PRODUCT_1);

        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Cart should have 1 item after removing one");
        Assert.assertFalse(cartPage.isItemInCart(PRODUCT_1),
                PRODUCT_1 + " should be removed from cart");
        Assert.assertTrue(cartPage.isItemInCart(PRODUCT_2),
                PRODUCT_2 + " should still be in cart");
    }
}
