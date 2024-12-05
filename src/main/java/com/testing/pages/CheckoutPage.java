package com.testing.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera explícita
    }

    private By paymentAddressButton = By.id("button-payment-address");
    private By shippingAddressButton = By.id("button-shipping-address");
    private By shippingMethodButton = By.id("button-shipping-method");
    private By agreeCheckbox = By.name("agree");
    private By paymentMethodButton = By.id("button-payment-method");
    private By confirmButton = By.id("button-confirm");

    // Completar el proceso de checkout
    public void completeCheckout() {
        try {
            // Intentar hacer clic en el botón de dirección de pago, si está disponible
            clickElementIfPresent(paymentAddressButton);

            // Intentar hacer clic en el botón de dirección de envío, si está disponible
            clickElementIfPresent(shippingAddressButton);

            // Intentar hacer clic en el botón de método de envío, si está disponible
            clickElementIfPresent(shippingMethodButton);

            // Esperar que la casilla de acuerdo se pueda marcar y marcarla si es necesario
            WebElement agreeCheckboxElement = wait.until(ExpectedConditions.elementToBeClickable(agreeCheckbox));
            if (!agreeCheckboxElement.isSelected()) {
                agreeCheckboxElement.click(); // Marcar la casilla si no está seleccionada
            }

            // Intentar hacer clic en el botón de método de pago, si está disponible
            clickElementIfPresent(paymentMethodButton);

            // Intentar hacer clic en el botón de confirmación, si está disponible
            clickElementIfPresent(confirmButton);

        } catch (Exception e) {
            throw new RuntimeException("Error durante el proceso de checkout: " + e.getMessage(), e);
        }
    }

    // Método para intentar hacer clic en un elemento si está presente
    private void clickElementIfPresent(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            if (element.isDisplayed() && element.isEnabled()) {
                element.click();
            }
        } catch (TimeoutException e) {
            System.out.println("Elemento no encontrado o no clickeable: " + locator);
        } catch (Exception e) {
            System.out.println("Error al hacer clic en el elemento: " + locator);
        }
    }

    // Verificar que la compra se ha completado por la URL
    public boolean isPurchaseComplete() {
        try {
            // Esperar a que la URL de la página cambie a la ruta de éxito
            wait.until(ExpectedConditions.urlContains("checkout/success"));
            return driver.getCurrentUrl().contains("checkout/success");
        } catch (TimeoutException e) {
            System.out.println("Tiempo de espera excedido al verificar la URL de éxito: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error al verificar la URL de éxito: " + e.getMessage());
            return false; // Si no se encuentra la URL correcta, retornamos falso
        }
    }
}
