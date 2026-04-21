package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.ProductDetailPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * InventoryTest - Test cases covering the Products / Inventory page.
 * TC_INV_01 to TC_INV_05
 */
@Epic("SauceDemo E2E Tests")
@Feature("Inventory / Products")
public class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeEach() {
        inventoryPage = openAndLogin();
        inventoryPage.waitForPageLoad();
    }

    // ──────────────────────────────────────────────
    //  TC_INV_01 - All 6 products are listed
    // ──────────────────────────────────────────────
    @Test(priority = 1, description = "Verify all 6 products are displayed on the inventory page")
    @Story("Product Listing")
    @Severity(SeverityLevel.NORMAL)
    @Description("The inventory page should list exactly 6 products for a standard user.")
    public void testAllProductsDisplayed() {
        int productCount = inventoryPage.getProductCount();
        Assert.assertEquals(productCount, 6,
                "Expected 6 products on the inventory page, but found: " + productCount);
    }

    // ──────────────────────────────────────────────
    //  TC_INV_02 - Sort by price: low to high
    // ──────────────────────────────────────────────
    @Test(priority = 2, description = "Verify products are sorted by price low to high")
    @Story("Product Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("After selecting 'Price (low to high)', product prices should be in ascending order.")
    public void testSortByPriceLowToHigh() {
        inventoryPage.sortProductsBy("Price (low to high)");

        List<Double> prices = inventoryPage.getAllProductPricesAsDouble();
        Assert.assertTrue(prices.size() > 0, "Price list should not be empty");

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1),
                    String.format("Prices not in ascending order at index %d: %.2f > %.2f",
                            i, prices.get(i), prices.get(i + 1)));
        }
    }

    // ──────────────────────────────────────────────
    //  TC_INV_03 - Sort by name: A to Z
    // ──────────────────────────────────────────────
    @Test(priority = 3, description = "Verify products are sorted alphabetically A to Z")
    @Story("Product Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("After selecting 'Name (A to Z)', product names should be in alphabetical order.")
    public void testSortByNameAToZ() {
        inventoryPage.sortProductsBy("Name (A to Z)");

        List<String> names = inventoryPage.getAllProductNames();
        Assert.assertTrue(names.size() > 0, "Product names list should not be empty");

        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0,
                    String.format("Names not in A-Z order at index %d: '%s' > '%s'",
                            i, names.get(i), names.get(i + 1)));
        }
    }

    // ──────────────────────────────────────────────
    //  TC_INV_04 - Product detail page opens correctly
    // ──────────────────────────────────────────────
    @Test(priority = 4, description = "Verify clicking a product name opens the correct detail page")
    @Story("Product Detail Navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clicking on a product name should navigate to the product's detail page.")
    public void testProductDetailPageOpens() {
        String targetProduct = "Sauce Labs Backpack";

        ProductDetailPage detailPage = inventoryPage.clickOnProductName(targetProduct);
        detailPage.waitForPageLoad();

        Assert.assertEquals(detailPage.getProductName(), targetProduct,
                "Product name on detail page does not match");
        Assert.assertTrue(detailPage.isProductImageDisplayed(),
                "Product image should be visible on detail page");
        Assert.assertTrue(detailPage.getCurrentUrl().contains("inventory-item"),
                "URL should contain 'inventory-item'");
    }

    // ──────────────────────────────────────────────
    //  TC_INV_05 - Back to products from detail page
    // ──────────────────────────────────────────────
    @Test(priority = 5, description = "Verify 'Back to Products' button navigates back to inventory")
    @Story("Navigation")
    @Severity(SeverityLevel.MINOR)
    @Description("Clicking 'Back to Products' from a detail page should return to the inventory list.")
    public void testBackToProductsFromDetailPage() {
        ProductDetailPage detailPage = inventoryPage.clickOnProductName("Sauce Labs Bike Light");
        detailPage.waitForPageLoad();

        InventoryPage backToInventory = detailPage.goBackToProducts();
        backToInventory.waitForPageLoad();

        Assert.assertTrue(backToInventory.getCurrentUrl().contains("inventory"),
                "URL should contain 'inventory' after going back");
        Assert.assertEquals(backToInventory.getPageTitle(), "Products",
                "Page title should be 'Products' after navigating back");
    }
}
