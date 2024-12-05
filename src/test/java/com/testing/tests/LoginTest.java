package com.testing.tests;

import com.testing.pages.LoginPage;
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

public class LoginTest {
    private WebDriver driver;
    private ExcelReporter excelReporter;

    @Before
    public void setUp() throws IOException {
        // 🌐 Configuración moderna para ChromeDriver
        WebDriverManager.chromedriver().setup();

        // ⚙️ Opciones de Chrome (opcional pero recomendado)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        // 🚀 Inicializar el driver
        driver = new ChromeDriver(options);
        driver.get("https://opencart.abstracta.us/");

        // 📊 Inicializar el reporte en Excel
        excelReporter = new ExcelReporter("src/test/resources/test_report.xlsx");
    }

    @Test
    public void testLogin() throws IOException {
        // 📂 Leer todas las credenciales desde el archivo Excel
        List<String[]> credentialsList = ExcelReader.readLoginData("src/test/resources/testdata.xlsx");

        // Validar que hay datos en el archivo Excel
        if (credentialsList.isEmpty()) {
            System.out.println("El archivo Excel no contiene credenciales para probar.");
            return;
        }

        int attempt = 1;

        // Iterar sobre todas las credenciales y ejecutar los tests
        for (String[] credentials : credentialsList) {
            String email = credentials[0].trim();
            String password = credentials[1].trim();

            // Detener la iteración si se encuentran credenciales vacías
            if (email.isEmpty() && password.isEmpty()) {
                System.out.println("Fin de los datos en el archivo Excel.");
                break;
            }

            System.out.println("\nIntento #" + attempt + " - Credenciales: " + email + " / " + password);

            String status = "❌ Error";
            String errorMessage = null;

            try {
                // 🖥️ Crear página de Login e iniciar sesión
                LoginPage loginPage = new LoginPage(driver);
                loginPage.goToLoginPage();
                loginPage.login(email, password);

                // ✅ Validar que se ha iniciado sesión correctamente
                boolean loggedIn = loginPage.isLoginSuccessful();
                assert loggedIn : "Error al iniciar sesión con: " + email;

                status = "✔️ Exitoso";
                System.out.println("✔️ Sesión iniciada con éxito para: " + email);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                System.out.println("⚠️ Error al intentar iniciar sesión con: " + email + " / " + password);
            }

            // 📝 Escribir el resultado del test en el archivo Excel
            excelReporter.writeTestResult("Login Attempt " + attempt, status, errorMessage);
            attempt++;
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
