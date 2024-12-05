package com.testing.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private By quantityField = By.id("input-quantity");
    private By addToCartButton = By.id("button-cart");
    private By successMessage = By.cssSelector(".alert-success");
    private By searchField = By.name("search");

    // Buscar un producto y agregarlo al carrito
    public void searchAndAddToCart(String productName, int quantity) {
        try {
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(searchField));
            searchBox.clear();
            searchBox.sendKeys(productName);
            Actions actions = new Actions(driver);
            actions.sendKeys(searchBox, Keys.RETURN).perform();

            // Esperar que aparezcan los resultados y hacer clic en el producto
            WebElement productLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(productName)));
            productLink.click();

            // Esperar el campo de cantidad, asegurarse de que es un valor numérico
            WebElement quantityBox = wait.until(ExpectedConditions.elementToBeClickable(quantityField));
            quantityBox.clear();
            if (quantity <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
            }
            quantityBox.sendKeys(String.valueOf(quantity));

            // Hacer clic en el botón de agregar al carrito
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
            addButton.click();

        } catch (Exception e) {
            System.out.println("Error al buscar y agregar el producto al carrito: " + e.getMessage());
            throw e;  // Re-throw para que el test falle si algo va mal
        }
    }

    // Verificar si el producto fue agregado al carrito
    public boolean isProductAddedToCart() {
        try {
            // Esperar la aparición de la alerta de éxito (mensaje de producto agregado)
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-success.alert-dismissible")));

            // Verificar si el mensaje contiene "Success" o "You have added"
            String messageText = successMessage.getText();
            return messageText.contains("Success") || messageText.contains("You have added");
        } catch (Exception e) {
            return false; // Si no aparece el mensaje, significa que el producto no se añadió correctamente
        }
    }

}
