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

    // Locators (elements positions on page)
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    /**
     * Constructor
     *
     * @param driver WebDriver instance to be used for all interactions
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigates to the login page
     */
    public void open() {
        driver.get("https://www.saucedemo.com/");
        logger.info("Öppnade login-sidan");
    }

    /**
     * Enters the username into the username field
     *
     * @param username the username to enter
     */
    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        field.clear();
        field.sendKeys(username);
        logger.debug("Ifyllt användarnamn: {}", username);
    }

    /**
     * Enters the password into the password field
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        field.clear();
        field.sendKeys(password);
        logger.debug("Ifyllt lösenord");
    }

    /**
     * Clicks the login button
     */
    public void clickLogin() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        button.click();
        logger.info("Klickade på login-knappen");
    }

    /**
     * Performs complete login action: fills username, password and clicks login button
     *
     * @param username username to use
     * @param password password to use
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Checks if an error message is currently displayed on the page
     *
     * @return true if error message is visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the text of the visible error message
     *
     * @return error message text
     * @throws TimeoutException if no error message appears within wait timeout
     */
    public String getErrorMessageText() {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        String text = error.getText();
        logger.info("Felmeddelande: {}", text);
        return text;
    }

    /**
     * Checks if the browser is currently on the login page
     *
     * @return true if current URL matches the login page URL
     */
    public boolean isLoginPage() {
        return driver.getCurrentUrl().equals("https://www.saucedemo.com/");
    }

    /**
     * Checks if the user has successfully navigated to the inventory page after login
     *
     * @return true if current URL contains "inventory.html"
     */
    public boolean isInventoryPage() {
        return driver.getCurrentUrl().contains("inventory.html");
    }
}