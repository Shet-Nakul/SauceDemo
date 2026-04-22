package com.saucedemo.pages;

import com.saucedemo.utils.ElementUtils;
import com.saucedemo.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * InventoryPage - Page Object Model for the Products / Inventory page.
 * URL: https://www.saucedemo.com/inventory.html
 */
public class InventoryPage {

    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitUtils waitUtils;

    // ──────────────────────────────────────────────
    //  Locators
    // ──────────────────────────────────────────────
    private static final By PAGE_TITLE =By.xpath("//span[@class='title']");

    private static final By INVENTORY_LIST =By.xpath("//div[@class='inventory_list']");

    private static final By ALL_PRODUCT_NAMES =By.xpath("//div[@class='inventory_item_name']");

    private static final By ALL_PRODUCT_PRICES =By.xpath("//div[@class='inventory_item_price']");

    private static final By ALL_ADD_TO_CART_BUTTONS =By.xpath("//button[contains(@data-test,'add-to-cart')]");

    private static final By ALL_REMOVE_BUTTONS =By.xpath("//button[contains(@data-test,'remove')]");

    private static final By SORT_DROPDOWN =By.xpath("//select[@data-test='product-sort-container']");

    private static final By CART_ICON =By.xpath("//a[@class='shopping_cart_link']");

    private static final By CART_BADGE =By.xpath("//span[@class='shopping_cart_badge']");

    private static final By BURGER_MENU_BUTTON =By.xpath("//button[@id='react-burger-menu-btn']");

    private static final By MENU_LOGOUT_LINK =By.xpath("//a[@id='logout_sidebar_link']");

    private static final By MENU_ALL_ITEMS_LINK =By.xpath("//a[@id='inventory_sidebar_link']");

    private static final By MENU_ABOUT_LINK =By.xpath("//a[@id='about_sidebar_link']");

    private static final By MENU_RESET_LINK =By.xpath("//a[@id='reset_sidebar_link']");

    private static final By MENU_CLOSE_BUTTON =By.xpath("//button[@id='react-burger-cross-btn']");

    // Dynamic locators (use String.format before passing to By.xpath)
    private static final String ADD_TO_CART_BY_NAME_TEMPLATE ="//div[text()='%s']/ancestor::div[@class='inventory_item']//button[contains(@data-test,'add-to-cart')]";

    private static final String REMOVE_BY_NAME_TEMPLATE ="//div[text()='%s']/ancestor::div[@class='inventory_item']//button[contains(@data-test,'remove')]";

    private static final String PRODUCT_LINK_BY_NAME_TEMPLATE ="//div[@class='inventory_item_name' and text()='%s']";

    // ──────────────────────────────────────────────
    //  Constructor
    // ──────────────────────────────────────────────
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        this.waitUtils = new WaitUtils(driver);
    }

    // ──────────────────────────────────────────────
    //  Page Actions
    // ──────────────────────────────────────────────

    /** Waits for Inventory page to load and verifies the page title is visible. */
    @Step("Wait for Inventory page to load")
    public InventoryPage waitForPageLoad() {
        waitUtils.waitForVisibility(PAGE_TITLE);
        waitUtils.waitForVisibility(INVENTORY_LIST);
        return this;
    }

    /** Add a product to cart by its name. */
    @Step("Add product '{productName}' to cart")
    public InventoryPage addToCartByName(String productName) {
        By locator = By.xpath(String.format(ADD_TO_CART_BY_NAME_TEMPLATE, productName));
        elementUtils.click(locator);
        return this;
    }

    /** Remove a product from cart by its name. */
    @Step("Remove product '{productName}' from cart")
    public InventoryPage removeFromCartByName(String productName) {
        By locator = By.xpath(String.format(REMOVE_BY_NAME_TEMPLATE, productName));
        elementUtils.click(locator);
        return this;
    }

    /** Click the first "Add to cart" button. */
    @Step("Add first product to cart")
    public InventoryPage addFirstProductToCart() {
        elementUtils.click(ALL_ADD_TO_CART_BUTTONS);
        return this;
    }

    /** Add all visible products to cart. */
    @Step("Add all products to cart")
    public InventoryPage addAllProductsToCart() {
        List<WebElement> buttons = driver.findElements(ALL_ADD_TO_CART_BUTTONS);
        for (WebElement btn : buttons) {
            btn.click();
        }
        return this;
    }

    /** Click on a product name to open its detail page. */
    @Step("Open product detail page for: {productName}")
    public ProductDetailPage clickOnProductName(String productName) {
        By locator = By.xpath(String.format(PRODUCT_LINK_BY_NAME_TEMPLATE, productName));
        elementUtils.click(locator);
        return new ProductDetailPage(driver);
    }

    /** Sort products using the sort dropdown. */
    @Step("Sort products by: {sortOption}")
    public InventoryPage sortProductsBy(String sortOption) {
        elementUtils.selectByVisibleText(SORT_DROPDOWN, sortOption);
        return this;
    }

    /** Navigate to cart page by clicking cart icon. */
    @Step("Click on Cart icon")
    public CartPage clickCartIcon() {
        elementUtils.click(CART_ICON);
        return new CartPage(driver);
    }

    /** Open the hamburger / burger menu. */
    @Step("Open burger menu")
    public InventoryPage openBurgerMenu() {
        elementUtils.click(BURGER_MENU_BUTTON);
        waitUtils.waitForVisibility(MENU_LOGOUT_LINK);
        return this;
    }

    /** Logout via burger menu. */
    @Step("Logout via burger menu")
    public LoginPage logout() {
        openBurgerMenu();
        elementUtils.click(MENU_LOGOUT_LINK);
        return new LoginPage(driver);
    }

    /** Click "Reset App State" in the burger menu. */
    @Step("Reset app state via burger menu")
    public InventoryPage resetAppState() {
        openBurgerMenu();
        elementUtils.click(MENU_RESET_LINK);
        elementUtils.click(MENU_CLOSE_BUTTON);
        return this;
    }

    // ──────────────────────────────────────────────
    //  Getters / Assertion helpers
    // ──────────────────────────────────────────────

    /** Returns the page heading text ("Products"). */
    public String getPageTitle() {
        return elementUtils.getText(PAGE_TITLE);
    }

    /** Returns list of all product name texts on the page. */
    public List<String> getAllProductNames() {
        return elementUtils.getTextList(ALL_PRODUCT_NAMES);
    }

    /** Returns list of all product price texts on the page. */
    public List<String> getAllProductPrices() {
        return elementUtils.getTextList(ALL_PRODUCT_PRICES);
    }

    /** Returns all product prices as doubles (strips '$'). */
    public List<Double> getAllProductPricesAsDouble() {
        return getAllProductPrices().stream()
                .map(p -> Double.parseDouble(p.replace("$", "").trim()))
                .collect(Collectors.toList());
    }

    /** Returns the number of items shown in the cart badge. Returns 0 if badge absent. */
    public int getCartBadgeCount() {
        if (!elementUtils.isDisplayed(CART_BADGE)) return 0;
        return Integer.parseInt(elementUtils.getText(CART_BADGE));
    }

    /** Returns true if cart badge is visible. */
    public boolean isCartBadgeVisible() {
        return elementUtils.isDisplayed(CART_BADGE);
    }

    /** Returns the total count of products listed on the page. */
    public int getProductCount() {
        return elementUtils.getElementCount(ALL_PRODUCT_NAMES);
    }

    /** Returns true if the inventory list is displayed (page loaded). */
    public boolean isInventoryListDisplayed() {
        return elementUtils.isDisplayed(INVENTORY_LIST);
    }

    /** Returns the current URL. */
    public String getCurrentUrl() {
        return elementUtils.getCurrentUrl();
    }

    /** Returns true if 'Remove' button is present for the given product. */
    public boolean isProductInCart(String productName) {
        By locator = By.xpath(String.format(REMOVE_BY_NAME_TEMPLATE, productName));
        return elementUtils.isDisplayed(locator);
    }
}
