package com.saucedemo.pages;

import com.saucedemo.utils.ElementUtils;
import com.saucedemo.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * CheckoutPage - Page Object Model for the two-step Checkout flow:
 *   Step 1: https://www.saucedemo.com/checkout-step-one.html  (Enter Info)
 *   Step 2: https://www.saucedemo.com/checkout-step-two.html  (Overview)
 *   Complete: https://www.saucedemo.com/checkout-complete.html
 */
public class CheckoutPage {

    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitUtils waitUtils;

    // ──────────────────────────────────────────────
    //  Locators – Step 1 (Info form)
    // ──────────────────────────────────────────────
    private static final By PAGE_TITLE =By.xpath("//span[@class='title']");

    private static final By FIRST_NAME_INPUT =By.xpath("//input[@data-test='firstName']");

    private static final By LAST_NAME_INPUT =By.xpath("//input[@data-test='lastName']");

    private static final By POSTAL_CODE_INPUT =By.xpath("//input[@data-test='postalCode']");

    private static final By CONTINUE_BUTTON =By.xpath("//input[@data-test='continue']");

    private static final By CANCEL_BUTTON =By.xpath("//button[@data-test='cancel']");

    private static final By ERROR_MESSAGE =By.xpath("//h3[@data-test='error']");

    // ──────────────────────────────────────────────
    //  Locators – Step 2 (Overview)
    // ──────────────────────────────────────────────
    private static final By OVERVIEW_ITEM_NAMES =By.xpath("//div[@class='inventory_item_name']");

    private static final By OVERVIEW_ITEM_PRICES =By.xpath("//div[@class='inventory_item_price']");

    private static final By ITEM_TOTAL_LABEL =By.xpath("//div[@class='summary_subtotal_label']");

    private static final By TAX_LABEL =By.xpath("//div[@class='summary_tax_label']");

    private static final By TOTAL_LABEL =By.xpath("//div[@class='summary_total_label']");

    private static final By FINISH_BUTTON =By.xpath("//button[@data-test='finish']");

    private static final By OVERVIEW_CANCEL_BUTTON =By.xpath("//button[@data-test='cancel']");

    // ──────────────────────────────────────────────
    //  Locators – Complete page
    // ──────────────────────────────────────────────
    private static final By COMPLETE_HEADER =By.xpath("//h2[@class='complete-header']");

    private static final By COMPLETE_TEXT =By.xpath("//div[@class='complete-text']");

    private static final By BACK_HOME_BUTTON =By.xpath("//button[@data-test='back-to-products']");

    private static final By PONY_EXPRESS_IMAGE =By.xpath("//img[@class='pony_express']");

    // ──────────────────────────────────────────────
    //  Constructor
    // ──────────────────────────────────────────────
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        this.waitUtils = new WaitUtils(driver);
    }

    // ──────────────────────────────────────────────
    //  Page Actions – Step 1
    // ──────────────────────────────────────────────

    @Step("Wait for Checkout Step 1 to load")
    public CheckoutPage waitForStep1Load() {
        waitUtils.waitForVisibility(FIRST_NAME_INPUT);
        return this;
    }

    @Step("Enter first name: {firstName}")
    public CheckoutPage enterFirstName(String firstName) {
        elementUtils.enterText(FIRST_NAME_INPUT, firstName);
        return this;
    }

    @Step("Enter last name: {lastName}")
    public CheckoutPage enterLastName(String lastName) {
        elementUtils.enterText(LAST_NAME_INPUT, lastName);
        return this;
    }

    @Step("Enter postal code: {postalCode}")
    public CheckoutPage enterPostalCode(String postalCode) {
        elementUtils.enterText(POSTAL_CODE_INPUT, postalCode);
        return this;
    }

    @Step("Fill checkout information")
    public CheckoutPage fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return this;
    }

    @Step("Click Continue button")
    public CheckoutPage clickContinue() {
        elementUtils.click(CONTINUE_BUTTON);
        return this;
    }

    @Step("Click Cancel (Step 1)")
    public CartPage clickCancelOnStep1() {
        elementUtils.click(CANCEL_BUTTON);
        return new CartPage(driver);
    }

    // ──────────────────────────────────────────────
    //  Page Actions – Step 2 (Overview)
    // ──────────────────────────────────────────────

    @Step("Wait for Checkout Overview (Step 2) to load")
    public CheckoutPage waitForStep2Load() {
        waitUtils.waitForVisibility(FINISH_BUTTON);
        return this;
    }

    @Step("Click Finish button")
    public CheckoutPage clickFinish() {
        elementUtils.click(FINISH_BUTTON);
        return this;
    }

    @Step("Click Cancel (Step 2)")
    public InventoryPage clickCancelOnStep2() {
        elementUtils.click(OVERVIEW_CANCEL_BUTTON);
        return new InventoryPage(driver);
    }

    // ──────────────────────────────────────────────
    //  Page Actions – Complete
    // ──────────────────────────────────────────────

    @Step("Click 'Back Home' on order complete page")
    public InventoryPage clickBackHome() {
        elementUtils.click(BACK_HOME_BUTTON);
        return new InventoryPage(driver);
    }

    // ──────────────────────────────────────────────
    //  Getters / Assertion helpers
    // ──────────────────────────────────────────────

    public String getPageTitle() {
        return elementUtils.getText(PAGE_TITLE);
    }

    public String getErrorMessage() {
        return elementUtils.getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return elementUtils.isDisplayed(ERROR_MESSAGE);
    }

    public List<String> getOverviewItemNames() {
        return elementUtils.getTextList(OVERVIEW_ITEM_NAMES);
    }

    public String getItemTotalText() {
        return elementUtils.getText(ITEM_TOTAL_LABEL);
    }

    public String getTaxText() {
        return elementUtils.getText(TAX_LABEL);
    }

    public String getTotalText() {
        return elementUtils.getText(TOTAL_LABEL);
    }

    public String getCompleteHeaderText() {
        return elementUtils.getText(COMPLETE_HEADER);
    }

    public String getCompleteText() {
        return elementUtils.getText(COMPLETE_TEXT);
    }

    public boolean isOrderCompleteImageDisplayed() {
        return elementUtils.isDisplayed(PONY_EXPRESS_IMAGE);
    }

    public String getCurrentUrl() {
        return elementUtils.getCurrentUrl();
    }
}
