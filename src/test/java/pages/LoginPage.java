package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("https://www.saucedemo.com/");
        logger.info("Öppnade login-sidan");
    }

    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        field.clear();
        field.sendKeys(username);
        logger.debug("Ifyllt användarnamn: {}", username);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        field.clear();
        field.sendKeys(password);
        logger.debug("Ifyllt lösenord");
    }

    public void clickLogin() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        button.click();
        logger.info("Klickade på login-knappen");
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        String text = error.getText();
        logger.info("Felmeddelande: {}", text);
        return text;
    }

    public boolean isLoginPage() {
        return driver.getCurrentUrl().equals("https://www.saucedemo.com/");
    }

    public boolean isInventoryPage() {
        return driver.getCurrentUrl().contains("inventory.html");
    }
}