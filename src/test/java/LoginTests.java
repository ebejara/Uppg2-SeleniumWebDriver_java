import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import pages.LoginPage;

/**
 * Test suite for login functionality on saucedemo.com
 * Uses Page Object pattern and JUnit 5 with ordered execution
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTests {

    private static WebDriver driver;
    private LoginPage loginPage;

    /**
     * One-time setup before all tests - initializes ChromeDriver in headless mode
     * Configured especially to work reliably in CI environments like GitHub Actions.
     */
    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        /// Headless configuration - essential for CI/Linux environments
        options.addArguments("--headless=new");              // Headless mode (new implementation)
        options.addArguments("--no-sandbox");                // Github Actions requested this
        options.addArguments("--disable-dev-shm-usage");     // Prevents /dev/shm issues in Docker/CI
        options.addArguments("--disable-gpu");               // Recommended in headless mode by Google
        options.addArguments("--window-size=1920,1080");     // Consistent viewport size for stability
        options.addArguments("--remote-allow-origins=*");    // Avoids CORS-related warnings

        // Disable Chrome password manager popups and features. When running tests, on local computers
        // these popups can interfere with test execution. Have to disable these features via prefs. so
        // that they do not show up at all as they may interfere with tests in CI environment as well.
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        // Additional useful arguments
        options.addArguments("--disable-infobars");

        // Kommentera ut --start-maximized i headless-läge (funkar inte på samma sätt)
        // options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
    }

    /**
     * Cleanup after all tests - closes the browser
     */
    @AfterAll
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Runs before each test - creates fresh LoginPage object and opens the login page
     */
    @BeforeEach
    public void setup() {
        loginPage = new LoginPage(driver);
        loginPage.open();
    }

    @Test
    @Order(1)
    @DisplayName("Lyckad inloggning")
    public void testSuccessfulLogin() {
        loginPage.login("standard_user", "secret_sauce");
        assertTrue(loginPage.isInventoryPage(), "Borde vara på inventory-sidan efter lyckad inloggning");
    }

    @Test
    @Order(2)
    @DisplayName("Fel användarnamn")
    public void testInvalidUsername() {
        loginPage.login("wrong_user", "secret_sauce");
        assertTrue(loginPage.isErrorMessageDisplayed(), "Felmeddelande borde visas");
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                loginPage.getErrorMessageText());
    }

    @Test
    @Order(3)
    @DisplayName("Fel lösenord")
    public void testInvalidPassword() {
        loginPage.login("standard_user", "wrong_password");
        assertTrue(loginPage.isErrorMessageDisplayed(), "Felmeddelande borde visas");
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                loginPage.getErrorMessageText());
    }
}