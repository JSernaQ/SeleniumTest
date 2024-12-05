package com.testing.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera explícita en caso de que sea necesario
    }

    // Ubicación del enlace al carrito
    private By cartLink = By.cssSelector("a[title='Shopping Cart']");

    // Ir al carrito
    public void goToCart() {
        // Hacer clic directamente en el enlace "Shopping Cart"
        driver.findElement(cartLink).click();
    }

    // Proceder al checkout
    public void goToCheckout() {
        // Esperar y hacer clic en el botón de checkout
        WebElement checkoutButtonElement = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Checkout")));
        checkoutButtonElement.click();
    }

}
