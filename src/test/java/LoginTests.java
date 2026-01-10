import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTests {

    private static WebDriver driver;
    private LoginPage loginPage;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    public void setup() {
        loginPage = new LoginPage(driver);
        loginPage.open();
    }

    @Test
    @Order(1)
    @DisplayName("G: Lyckad inloggning")
    public void testSuccessfulLogin() {
        loginPage.login("standard_user", "secret_sauce");
        assertTrue(loginPage.isInventoryPage(), "Borde vara på inventory-sidan efter lyckad inloggning");
    }

    @Test
    @Order(2)
    @DisplayName("VG: Fel användarnamn")
    public void testInvalidUsername() {
        loginPage.login("wrong_user", "secret_sauce");
        assertTrue(loginPage.isErrorMessageDisplayed(), "Felmeddelande borde visas");
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                loginPage.getErrorMessageText());
    }

    @Test
    @Order(3)
    @DisplayName("VG: Fel lösenord")
    public void testInvalidPassword() {
        loginPage.login("standard_user", "wrong_password");
        assertTrue(loginPage.isErrorMessageDisplayed(), "Felmeddelande borde visas");
        assertEquals("Epic sadface: Username and password do not match any user in this service",
                loginPage.getErrorMessageText());
    }
}