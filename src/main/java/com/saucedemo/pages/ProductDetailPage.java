package com.saucedemo.pages;

import com.saucedemo.utils.ElementUtils;
import com.saucedemo.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * ProductDetailPage - Page Object Model for the individual product detail page.
 * URL pattern: https://www.saucedemo.com/inventory-item.html?id=X
 */
public class ProductDetailPage {

    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitUtils waitUtils;

    // ──────────────────────────────────────────────
    //  Locators
    // ──────────────────────────────────────────────
    private static final By PRODUCT_NAME =By.xpath("//div[@class='inventory_details_name large_size']");

    private static final By PRODUCT_DESCRIPTION =By.xpath("//div[@class='inventory_details_desc large_size']");

    private static final By PRODUCT_PRICE =By.xpath("//div[@class='inventory_details_price']");

    private static final By PRODUCT_IMAGE =By.xpath("//img[@class='inventory_details_img']");

    private static final By ADD_TO_CART_BUTTON =By.xpath("//button[contains(@data-test,'add-to-cart')]");

    private static final By REMOVE_FROM_CART_BUTTON =By.xpath("//button[contains(@data-test,'rem')]");

    private static final By BACK_TO_PRODUCTS_BUTTON =By.xpath("//button[@data-test='back-to-pro']");

    private static final By CART_ICON =By.xpath("//a[@class='shopping_cart']");

    private static final By CART_BADGE =By.xpath("//span[@class='shopping_cart_badge']");

    // ──────────────────────────────────────────────
    //  Constructor
    // ──────────────────────────────────────────────
    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        this.waitUtils = new WaitUtils(driver);
    }

    // ──────────────────────────────────────────────
    //  Page Actions
    // ──────────────────────────────────────────────

    @Step("Wait for Product Detail page to load")
    public ProductDetailPage waitForPageLoad() {
        waitUtils.waitForVisibility(PRODUCT_NAME);
        return this;
    }

    @Step("Add product to cart from detail page")
    public ProductDetailPage addToCart() {
        elementUtils.click(ADD_TO_CART_BUTTON);
        return this;
    }

    @Step("Remove product from cart on detail page")
    public ProductDetailPage removeFromCart() {
        elementUtils.click(REMOVE_FROM_CART_BUTTON);
        return this;
    }

    @Step("Click 'Back to Products' button")
    public InventoryPage goBackToProducts() {
        elementUtils.click(BACK_TO_PRODUCTS_BUTTON);
        return new InventoryPage(driver);
    }

    @Step("Navigate to Cart from detail page")
    public CartPage goToCart() {
        elementUtils.click(CART_ICON);
        return new CartPage(driver);
    }

    // ──────────────────────────────────────────────
    //  Getters / Assertion helpers
    // ──────────────────────────────────────────────

    public String getProductName() {
        return elementUtils.getText(PRODUCT_NAME);
    }

    public String getProductDescription() {
        return elementUtils.getText(PRODUCT_DESCRIPTION);
    }

    public String getProductPrice() {
        return elementUtils.getText(PRODUCT_PRICE);
    }

    public double getProductPriceAsDouble() {
        return Double.parseDouble(getProductPrice().replace("$", "").trim());
    }

    public boolean isProductImageDisplayed() {
        return elementUtils.isDisplayed(PRODUCT_IMAGE);
    }

    public boolean isAddToCartButtonDisplayed() {
        return elementUtils.isDisplayed(ADD_TO_CART_BUTTON);
    }

    public boolean isRemoveButtonDisplayed() {
        return elementUtils.isDisplayed(REMOVE_FROM_CART_BUTTON);
    }

    public int getCartBadgeCount() {
        if (!elementUtils.isDisplayed(CART_BADGE)) return 0;
        return Integer.parseInt(elementUtils.getText(CART_BADGE));
    }

    public String getCurrentUrl() {
        return elementUtils.getCurrentUrl();
    }
}
