# SauceDemo Selenium TestNG Automation Framework

A production-ready, Maven-based Selenium test automation framework for [SauceDemo](https://www.saucedemo.com/),
built with **Java 17**, **TestNG**, and **Allure Reports**, following the **Page Object Model (POM)** pattern.

---

## 📁 Project Structure

```
saucedemo-selenium/
├── pom.xml                                          ← Maven build + all dependencies
├── README.md
└── src/
    ├── main/java/com/saucedemo/
    │   ├── pages/                                   ← Page Object Model classes
    │   │   ├── LoginPage.java
    │   │   ├── InventoryPage.java
    │   │   ├── ProductDetailPage.java
    │   │   ├── CartPage.java
    │   │   └── CheckoutPage.java
    │   └── utils/                                   ← Shared utility classes
    │       ├── ConfigReader.java                    ← Reads config.properties (Singleton)
    │       ├── DriverManager.java                   ← Thread-safe WebDriver lifecycle
    │       ├── WaitUtils.java                       ← Explicit wait helpers
    │       ├── ElementUtils.java                    ← Common element interactions
    │       └── ScreenshotUtils.java                 ← Screenshot capture + Allure attach
    └── test/
        ├── java/com/saucedemo/tests/
        │   ├── BaseTest.java                        ← @Before/@After + shared helpers
        │   ├── LoginTest.java                       ← 5 login test cases
        │   ├── InventoryTest.java                   ← 5 inventory/product test cases
        │   ├── CartTest.java                        ← 5 cart test cases
        │   ├── CheckoutTest.java                    ← 5 checkout test cases
        │   └── E2ETest.java                         ← 5 end-to-end test cases
        └── resources/
            ├── testng.xml                           ← TestNG suite definition
            ├── config.properties                    ← Runtime configuration
            └── allure.properties                    ← Allure results path
```

---

## 🧱 Framework Architecture

### Design Patterns
| Pattern | Usage |
|---|---|
| **Page Object Model** | Each page has its own class; locators + actions are encapsulated |
| **Fluent / Method Chaining** | Page actions return `this` or target page for readable test steps |
| **Singleton** | `ConfigReader` — one instance reads config for the whole run |
| **ThreadLocal** | `DriverManager` — safe for parallel test execution |

### Key Utilities
| Class | Responsibility |
|---|---|
| `ConfigReader` | Load `config.properties`; provide typed getters |
| `DriverManager` | Init / get / quit WebDriver per thread |
| `WaitUtils` | All explicit waits (visibility, clickability, URL, text, etc.) |
| `ElementUtils` | Click, type, getText, select, scroll, JS-click, hover |
| `ScreenshotUtils` | Capture to disk + attach to Allure on failure |

---

## ✅ Test Cases (20 Total)

| Class | ID | Description |
|---|---|---|
| `LoginTest` | TC_LOGIN_01 | Valid login → lands on Products page |
| `LoginTest` | TC_LOGIN_02 | Invalid password → error message |
| `LoginTest` | TC_LOGIN_03 | Locked user → locked error shown |
| `LoginTest` | TC_LOGIN_04 | Empty credentials → validation error |
| `LoginTest` | TC_LOGIN_05 | Logout → redirects to login page |
| `InventoryTest` | TC_INV_01 | 6 products listed for standard user |
| `InventoryTest` | TC_INV_02 | Sort by Price low→high is ascending |
| `InventoryTest` | TC_INV_03 | Sort by Name A→Z is alphabetical |
| `InventoryTest` | TC_INV_04 | Product detail page opens correctly |
| `InventoryTest` | TC_INV_05 | Back to Products navigates back |
| `CartTest` | TC_CART_01 | Add single item → badge shows 1 |
| `CartTest` | TC_CART_02 | Add two items → badge shows 2 |
| `CartTest` | TC_CART_03 | Remove item on inventory → badge clears |
| `CartTest` | TC_CART_04 | Cart page lists correct products |
| `CartTest` | TC_CART_05 | Remove item from cart page |
| `CheckoutTest` | TC_CHK_01 | Full checkout → order confirmation |
| `CheckoutTest` | TC_CHK_02 | Missing first name → error |
| `CheckoutTest` | TC_CHK_03 | Missing postal code → error |
| `CheckoutTest` | TC_CHK_04 | Overview page shows correct product + totals |
| `CheckoutTest` | TC_CHK_05 | Cancel overview → back to inventory |
| `E2ETest` | TC_E2E_01 | Full purchase flow end-to-end |
| `E2ETest` | TC_E2E_02 | Add from detail page + checkout |
| `E2ETest` | TC_E2E_03 | Continue Shopping returns to inventory |
| `E2ETest` | TC_E2E_04 | Sort High→Low, first item most expensive |
| `E2ETest` | TC_E2E_05 | Detail page content all non-empty |

---

## 🚀 Prerequisites

| Tool | Version |
|---|---|
| Java | 17+ |
| Maven | 3.8+ |
| Chrome / Firefox / Edge | Latest stable |
| Allure CLI *(optional, for HTML report)* | 2.x |

> **Note:** WebDriverManager (`io.github.bonigarcia`) automatically downloads and configures the correct browser driver binary. No manual `chromedriver` setup needed.

---

## ⚙️ Configuration

Edit `src/test/resources/config.properties`:

```properties
base.url=https://www.saucedemo.com/
browser=chrome          # chrome | firefox | edge
headless=false          # true for CI pipelines

valid.username=standard_user
valid.password=secret_sauce
locked.username=locked_out_user
invalid.password=wrong_password_123

implicit.wait=10
explicit.wait=15
```

---

## ▶️ Running Tests

### Run all tests
```bash
mvn clean test
```

### Run a specific test class
```bash
mvn clean test -Dtest=LoginTest
mvn clean test -Dtest=CartTest
```

### Run in headless mode (CI)
```bash
mvn clean test -Dheadless=true
```

### Run with a different browser
```bash
mvn clean test -Dbrowser=firefox
```

---

## 📊 Allure Report

### Generate and open the report
```bash
# Step 1 - run tests (results written to target/allure-results)
mvn clean test

# Step 2 - generate HTML report
mvn allure:report

# Step 3 - serve the report (opens browser automatically)
mvn allure:serve
```

Or with the Allure CLI:
```bash
allure serve target/allure-results
```

---

## 🔧 Tips

- **CI/CD**: Set `headless=true` in `config.properties` or pass `-Dheadless=true` to Maven.
- **Screenshots on failure**: Automatically captured and embedded in the Allure report.
- **Parallel execution**: `DriverManager` uses `ThreadLocal` — safe to set `parallel="methods"` or `parallel="classes"` in `testng.xml`.
- **Extend tests**: Add a new `XxxPage.java` in `pages/`, a new `XxxTest.java` extending `BaseTest`, and register the class in `testng.xml`.
