import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTests {

    private static WebDriver driver;
    private final String baseUrl = "https://www.saucedemo.com/";

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();

        // Konfigurera ChromeOptions för att stänga av password-relaterade popups
        ChromeOptions options = new ChromeOptions();

        // Viktiga preferences för att undvika "Change your password" popup
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);  // ← Denna stänger av leak detection!

        options.setExperimentalOption("prefs", prefs);

        // Extra bra för automatisering
        options.addArguments("--disable-infobars");          // Tar bort "Chrome kontrolleras av..."
        options.addArguments("--start-maximized");           // Maximerar fönstret direkt

        driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    public void openSite() {
        driver.get(baseUrl);
    }

    // -------------------
    // G – Grundtest: Lyckad inloggning
    // -------------------
    @Test
    @Order(1)
    @DisplayName("G: Lyckad inloggning")
    public void testSuccessfulLogin() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));

        String currentUrl = driver.getCurrentUrl();
        assertEquals("https://www.saucedemo.com/inventory.html", currentUrl);
    }

    // -------------------
    // VG: Fel användarnamn
    // -------------------
    @Test
    @Order(2)
    @DisplayName("VG: Fel användarnamn")
    public void testInvalidUsername() {
        driver.findElement(By.id("user-name")).sendKeys("wrong_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h3[data-test='error']")
        )).getText();

        assertEquals("Epic sadface: Username and password do not match any user in this service", error);
    }

    // -------------------
    // VG: Fel lösenord
    // -------------------
    @Test
    @Order(3)
    @DisplayName("VG: Fel lösenord")
    public void testInvalidPassword() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h3[data-test='error']")
        )).getText();

        assertEquals("Epic sadface: Username and password do not match any user in this service", error);
    }
}