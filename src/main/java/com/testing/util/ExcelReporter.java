package com.testing.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelReporter {

    private Workbook workbook;
    private Sheet sheet;
    private FileOutputStream fileOut;

    public ExcelReporter(String filePath) throws IOException {
        workbook = new XSSFWorkbook();  // Crear el workbook
        sheet = workbook.createSheet("Test Report");  // Crear la hoja de reporte
        fileOut = new FileOutputStream(new File(filePath));  // Crear el archivo de salida
        createHeader();  // Crear los encabezados
    }

    // Crear los encabezados
    private void createHeader() {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Test Name");
        headerRow.createCell(1).setCellValue("Status");
        headerRow.createCell(2).setCellValue("Error Message");
    }

    // Escribir un resultado de un test
    public void writeTestResult(String testName, String status, String errorMessage) {
        int rowCount = sheet.getPhysicalNumberOfRows();
        Row row = sheet.createRow(rowCount);
        row.createCell(0).setCellValue(testName);
        row.createCell(1).setCellValue(status);
        row.createCell(2).setCellValue(errorMessage != null ? errorMessage : "N/A");
    }

    // Guardar el reporte
    public void saveReport(String filePath) throws IOException {
        workbook.write(fileOut);  // Escribir en el archivo
        fileOut.close();  // Cerrar el archivo
    }

    public void close() throws IOException {
        workbook.close();
    }
}
