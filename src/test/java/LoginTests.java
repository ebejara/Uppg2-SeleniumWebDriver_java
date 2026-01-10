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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTests {

    private static WebDriver driver;
    private LoginPage loginPage;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Headless-inställningar – viktiga för GitHub Actions (Linux) och bra för prestanda
        options.addArguments("--headless=new");              // Modernt headless-läge
        options.addArguments("--no-sandbox");                // Krävs ofta på Linux/CI
        options.addArguments("--disable-dev-shm-usage");     // Förhindrar /dev/shm-problem i CI
        options.addArguments("--disable-gpu");               // Rekommenderas i headless
        options.addArguments("--window-size=1920,1080");     // Fast storlek för konsistens
        options.addArguments("--remote-allow-origins=*");    // Undviker CORS-varningar

        // Dina befintliga password-manager-inställningar
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);

        // Övriga bra inställningar
        options.addArguments("--disable-infobars");

        // Kommentera ut --start-maximized i headless-läge (funkar inte på samma sätt)
        // options.addArguments("--start-maximized");

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