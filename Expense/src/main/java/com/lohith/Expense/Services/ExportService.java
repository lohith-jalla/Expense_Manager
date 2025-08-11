package com.lohith.Expense.Services;

import com.lohith.Expense.Model.Expense;
import com.lohith.Expense.Repo.ExpenseRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    private final ExpenseRepo expenseRepository;

    public ExportService(ExpenseRepo expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ByteArrayInputStream exportExpensesToExcel(Long userId) throws IOException {
        String[] columns = {"ID", "Title", "Category", "Amount", "Date"};
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

        // Header row
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Fetch all pages
        int page = 0;
        int rowNum = 1;
        Page<Expense> expensePage;
        do {
            expensePage = expenseRepository.findAllByUserId(userId, PageRequest.of(page, 50)); // batch of 50
            List<Expense> expenses = expensePage.getContent();

            for (Expense expense : expenses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(expense.getId());
                row.createCell(1).setCellValue(expense.getName());
                row.createCell(2).setCellValue(expense.getDescription());
                row.createCell(3).setCellValue(expense.getAmount());
                row.createCell(4).setCellValue(expense.getDate().toString());
            }
            page++;
        } while (!expensePage.isLast());

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
