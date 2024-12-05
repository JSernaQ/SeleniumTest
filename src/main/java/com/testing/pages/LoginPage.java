package com.testing.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;

    // Localizadores
    @FindBy(linkText = "My Account")
    WebElement myAccountLink;

    @FindBy(linkText = "Login")
    WebElement loginLink;

    @FindBy(id = "input-email")
    WebElement emailField;

    @FindBy(id = "input-password")
    WebElement passwordField;

    @FindBy(xpath = "//input[@value='Login']")
    WebElement loginButton;

    @FindBy(xpath = "//div[@class='alert alert-danger']")
    WebElement loginErrorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        PageFactory.initElements(driver, this);
    }

    public void goToLoginPage() {
        myAccountLink.click();
        loginLink.click();
    }

    public void login(String email, String password) {
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        loginButton.click();
    }

    public boolean isLoginSuccessful() {
        try {
            // Esperar hasta que la URL contenga la ruta correcta después del login exitoso
            wait.until(ExpectedConditions.urlContains("route=account/account"));
            return true; // Si la URL es correcta, el login es exitoso
        } catch (TimeoutException e) {
            // Si no se encuentra la URL, se verifica si aparece un mensaje de error de login
            return isLoginErrorDisplayed();
        }
    }


    public boolean isLoginErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(loginErrorMessage));
            return loginErrorMessage.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void logout() {
        try {
            WebElement logoutButton = driver.findElement(By.linkText("Logout")); // Ajusta el selector según sea necesario
            logoutButton.click();
            System.out.println("✔ Sesión cerrada correctamente.");
        } catch (NoSuchElementException e) {
            System.out.println("⚠ No se encontró el botón de logout. Es posible que no haya sesión activa.");
        }
    }

}
