package com.client_db.webapp.service;

import com.client_db.webapp.models.Client;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelExportService {

    public ByteArrayOutputStream exportClientsToExcel(List<Client> clients) throws
            Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clients");

        // Header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Create Header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "First Name", "Last Name", "Email", "Phone",
                             "Address", "Status", "Created At"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

        // Compile with dates
        int rowNum = 1;
            for (Client client : clients) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(client.getId());
                row.createCell(1).setCellValue(client.getFirstName());
                row.createCell(2).setCellValue(client.getLastName());
                row.createCell(3).setCellValue(client.getEmail());
                row.createCell(4).setCellValue(client.getPhone());
                row.createCell(5).setCellValue(client.getAddress());
                row.createCell(6).setCellValue(client.getStatus());
                row.createCell(7).setCellValue(client.getCreatedAt().toString());
            }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write into a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;

    }
}
