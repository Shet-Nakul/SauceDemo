package com.saucedemo.pages;

import com.saucedemo.utils.ElementUtils;
import com.saucedemo.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * CartPage - Page Object Model for the Shopping Cart page.
 * URL: https://www.saucedemo.com/cart.html
 */
public class CartPage {

    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitUtils waitUtils;

    // ──────────────────────────────────────────────
    //  Locators
    // ──────────────────────────────────────────────
    private static final By PAGE_TITLE =By.xpath("//span[@class='ti']");

    private static final By CART_ITEMS =By.xpath("//div[@class='cart_item']");

    private static final By CART_ITEM_NAMES =By.xpath("//div[@class='inventory_item_name']");

    private static final By CART_ITEM_PRICES =By.xpath("//div[@class='inventory_item']");

    private static final By CART_ITEM_QUANTITIES =By.xpath("//div[@class='cart_quantity']");

    private static final By CONTINUE_SHOPPING_BUTTON =By.xpath("//button[@data-test='continue-shopping']");

    private static final By CHECKOUT_BUTTON =By.xpath("//button[@data-test='checkout']");

    // Dynamic remove-by-name template
    private static final String REMOVE_ITEM_BY_NAME_TEMPLATE =
            "//div[text()='%s']/ancestor::div[@class='cart_item']//button[contains(@data-test,'remove')]";

    // ──────────────────────────────────────────────
    //  Constructor
    // ──────────────────────────────────────────────
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        this.waitUtils = new WaitUtils(driver);
    }

    // ──────────────────────────────────────────────
    //  Page Actions
    // ──────────────────────────────────────────────

    @Step("Wait for Cart page to load")
    public CartPage waitForPageLoad() {
        waitUtils.waitForVisibility(PAGE_TITLE);
        return this;
    }

    @Step("Click 'Continue Shopping'")
    public InventoryPage clickContinueShopping() {
        waitUtils.waitForVisibility(CONTINUE_SHOPPING_BUTTON);
        elementUtils.click(CONTINUE_SHOPPING_BUTTON);
        return new InventoryPage(driver);
    }

    @Step("Click 'Checkout'")
    public CheckoutPage clickCheckout() {
        elementUtils.click(CHECKOUT_BUTTON);
        return new CheckoutPage(driver);
    }

    @Step("Remove item '{itemName}' from cart")
    public CartPage removeItemByName(String itemName) {
        By locator = By.xpath(String.format(REMOVE_ITEM_BY_NAME_TEMPLATE, itemName));
        return this;
    }

    // ──────────────────────────────────────────────
    //  Getters / Assertion helpers
    // ──────────────────────────────────────────────

    public String getPageTitle() {
        return elementUtils.getText(PAGE_TITLE);
    }

    public int getCartItemCount() {
        return elementUtils.getElementCount(CART_ITEMS);
    }

    public List<String> getCartItemNames() {
        return elementUtils.getTextList(CART_ITEM_NAMES);
    }

    public List<String> getCartItemPrices() {
        return elementUtils.getTextList(CART_ITEM_PRICES);
    }

    public boolean isItemInCart(String itemName) {
        return getCartItemNames().contains(itemName);
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }

    public String getCurrentUrl() {
        return elementUtils.getCurrentUrl();
    }
}
