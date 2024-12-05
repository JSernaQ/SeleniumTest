package com.testing.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    // Método para leer datos de login
    public static List<String[]> readLoginData(String filePath) throws IOException {
        List<String[]> credentialsList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("LoginData");

            // Iterar desde la fila 1 (ignorando la cabecera)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String email = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
                    String password = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
                    credentialsList.add(new String[]{email, password});
                }
            }
        }

        return credentialsList;
    }

    // Método para leer datos de productos
    public static List<String[]> readProductsData(String filePath) throws IOException {
        List<String[]> productsList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("ProductsData");  // Usar la hoja 'ProductsData'

            // Iterar sobre las filas para obtener los productos y las cantidades
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String productName = "";
                    int quantity = 0;

                    // Verificar si la celda del nombre del producto es de tipo texto o numérico
                    Cell productCell = row.getCell(0);
                    if (productCell != null) {
                        if (productCell.getCellType() == CellType.STRING) {
                            productName = productCell.getStringCellValue();
                        } else if (productCell.getCellType() == CellType.NUMERIC) {
                            productName = String.valueOf(productCell.getNumericCellValue()); // Convertir a texto
                        }
                    }

                    // Verificar si la celda de la cantidad tiene un valor numérico
                    Cell quantityCell = row.getCell(1);
                    if (quantityCell != null && quantityCell.getCellType() == CellType.NUMERIC) {
                        quantity = (int) quantityCell.getNumericCellValue();
                    }

                    // Añadir los datos a la lista
                    productsList.add(new String[]{productName, String.valueOf(quantity)});
                }
            }
        }

        return productsList;
    }
}
