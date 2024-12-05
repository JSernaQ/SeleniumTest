package com.testing.tests;

import com.testing.pages.*;
import com.testing.util.ExcelReader;
import com.testing.util.ExcelReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.List;

public class PurchaseTest {
    WebDriver driver;
    ExcelReporter excelReporter;

    @Before
    public void setUp() throws IOException {
        // 🌐 Configuración moderna para ChromeDriver
        WebDriverManager.chromedriver().setup();

        // ⚙️ Opciones de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        // 🚀 Inicializar el driver
        driver = new ChromeDriver(options);
        driver.get("https://opencart.abstracta.us/");  // Página principal de OpenCart

        // 📊 Inicializar el reporte en Excel
        excelReporter = new ExcelReporter("src/test/resources/test_report.xlsx");
    }

    @Test
    public void testPurchase() throws IOException {
        // 📂 Leer productos desde el archivo Excel
        List<String[]> productsList = ExcelReader.readProductsData("src/test/resources/testdata.xlsx");

        // 🛒 Automatizar el flujo de compra para cada producto
        for (String[] productData : productsList) {
            String productName = productData[0];
            int quantity = Integer.parseInt(productData[1]);
            System.out.println("\n\n🔍 Comprando producto: " + productName + " con cantidad: " + quantity);

            String status = "❌ Error";
            String errorMessage = null;

            try {
                // 🔑 Iniciar sesión antes de comprar
                System.out.println("🔑 Iniciando sesión...");
                LoginPage loginPage = new LoginPage(driver);
                loginPage.goToLoginPage();
                loginPage.login("ejemplo@ejemplo.mx", "12345");

                // ⏳ Pequeño retraso antes de continuar
                Thread.sleep(1000); // 1 segundo de retraso

                // 🔍 Buscar y agregar el producto al carrito
                System.out.println("🔍 Buscando y añadiendo el producto al carrito...");
                ProductPage productPage = new ProductPage(driver);
                productPage.searchAndAddToCart(productName, quantity);

                // ⏳ Pequeño retraso después de agregar el producto
                Thread.sleep(1000); // 1 segundo de retraso

                // ✅ Verificar si el producto se ha agregado al carrito
                boolean productAdded = productPage.isProductAddedToCart();
                assert productAdded : "El producto " + productName + " no se añadió correctamente al carrito";

                // 🛒 Ir al carrito y proceder al checkout
                System.out.println("🛒 Procediendo al carrito...");
                CartPage cartPage = new CartPage(driver);
                cartPage.goToCart();
                cartPage.goToCheckout();

                // 💳 Completar el proceso de checkout
                System.out.println("💳 Completar el proceso de compra...");
                CheckoutPage checkoutPage = new CheckoutPage(driver);
                checkoutPage.completeCheckout();
                System.out.println("✅ Proceso de compra completado para el producto: " + productName);

                // ✅ Validar que la compra se completó con éxito
                boolean purchaseComplete = checkoutPage.isPurchaseComplete();
                assert purchaseComplete : "La compra no se completó con éxito para el producto " + productName;

                System.out.println("🎉 Compra exitosa para el producto: " + productName);

                status = "✔️ Exitoso";

                // 🚪 Cerrar sesión
                System.out.println("🚪 Cerrando sesión...");
                loginPage.logout();

                // ⏳ Esperar que la página vuelva al estado inicial
                driver.get("https://opencart.abstracta.us/index.php?route=account/logout");

            } catch (Exception e) {
                errorMessage = e.getMessage();
                System.out.println("⚠️ Error durante la compra del producto " + productData[0] + ": " + e.getMessage());
            }

            // 📝 Escribir el resultado del test en el archivo Excel
            excelReporter.writeTestResult(productName, status, errorMessage);

        }

        // 💾 Guardar el reporte después de todas las compras
        excelReporter.saveReport("src/test/resources/test_report.xlsx");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            // 🚶‍♂️ Cerrar el navegador al final de todas las pruebas
            driver.quit();
        }
    }
}
