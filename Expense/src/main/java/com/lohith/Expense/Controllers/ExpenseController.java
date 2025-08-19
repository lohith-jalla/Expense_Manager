package com.lohith.Expense.Controllers;

import com.lohith.Expense.Dto.PatchUpdateDto;
import com.lohith.Expense.Model.Expense;
import com.lohith.Expense.Model.ExpenseType;
import com.lohith.Expense.Services.ExportService;
import com.lohith.Expense.Services.ServiceImpl.ExpenseServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController()
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseServiceImpl expenseServiceImpl;
    private final ExportService exportService;


    // Advanced Api for filters and other Features.

    // Added Pagination
    @GetMapping("/search")
    public ResponseEntity<Page<Object>> searchExpenses(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestHeader("Authorization") String header
    ) {
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Object> result = expenseServiceImpl.searchExpenses(query, userId,pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/progress")
    public ResponseEntity<Page<Object>> getMonthlyExpense(
            @RequestParam Long period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestHeader("Authorization") String header
    ){
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pageable pageable=PageRequest.of(page,size,Sort.by("date").descending());
        Long userId= expenseServiceImpl.extractUserId(header);
        return new ResponseEntity<>(expenseServiceImpl.getMonthlyProgress(userId,period,pageable),HttpStatus.OK);
    }

    @GetMapping("/summary/type")
    public ResponseEntity<Map<ExpenseType, Double>> getSummaryByType(
            @RequestHeader("Authorization") String header,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);

        Map<ExpenseType, Double> summary = expenseServiceImpl.getExpenseSummaryByType(userId, startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<Map<String, Double>> getMonthlySummary(
            @RequestHeader("Authorization") String header,
            @RequestParam int year
    ) {
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        Map<String, Double> summary = expenseServiceImpl.getMonthlySummary(userId, year);
        return ResponseEntity.ok(summary);
    }

    // Export Expenses to Excel sheet
    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExpenses(
            @RequestHeader("Authorization") String header
    ) throws IOException {

        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);

        ByteArrayInputStream excelData = exportService.exportExpensesToExcel(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=expenses.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelData));
    }


    // Added Pagination
    @GetMapping()
    public ResponseEntity<Page<Expense>> getExpenseByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestHeader("Authorization") String Header
    ){
        boolean isValidToken= expenseServiceImpl.validateToken(Header);

        if(!isValidToken){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long userId = expenseServiceImpl.extractUserId(Header);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return new ResponseEntity<>(expenseServiceImpl.getExpensesByUserId(userId,pageable),HttpStatus.OK);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> getExpenseById(
            @RequestHeader("Authorization") String header,
            @PathVariable("expenseId") Long expenseId
    ) {
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = expenseServiceImpl.extractUserId(header);
        Expense expense = expenseServiceImpl.getExpenseByUserIdAndExpenseId(userId, expenseId);

        return ResponseEntity.ok(expense);
    }

    @PostMapping()
    public ResponseEntity<Expense> createExpense(
            @RequestHeader("Authorization") String header,
            @Valid @RequestBody Expense expense
    ){
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        return new ResponseEntity<>(expenseServiceImpl.createExpense(expense,userId),HttpStatus.CREATED);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateExpense(
            @RequestHeader("Authorization") String header,
            @PathVariable("expenseId") Long  expenseId,
            @Valid @RequestBody PatchUpdateDto expense
    ){
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        Expense UpdatedExpense= expenseServiceImpl.updateExpense(expense,userId,expenseId);
        if(UpdatedExpense!=null){
            return ResponseEntity.ok(UpdatedExpense);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpenseById(
            @RequestHeader("Authorization") String header,
            @PathVariable("expenseId") Long  expenseId
    ){
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        boolean isRemoved= expenseServiceImpl.deleteExpense(expenseId,userId);
        if(isRemoved){
            return new ResponseEntity<>("Expense with id "+  expenseId +" Deleted Successfully",HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
