package com.lohith.Expense.Controllers;

import com.lohith.Expense.Dto.MonthlyExpenseDto;
import com.lohith.Expense.Dto.PatchUpdateDto;
import com.lohith.Expense.Model.Expense;
import com.lohith.Expense.Model.ExpenseType;
import com.lohith.Expense.Services.ServiceImpl.ExpenseServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseServiceImpl expenseServiceImpl;


    // Advanced Api for filters and other Features.

    @GetMapping("/search")
    public ResponseEntity<List<Object>> searchExpenses(
            @RequestParam String query,
            @RequestHeader("Authorization") String header
    ) {
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        List<Object> result = expenseServiceImpl.searchExpenses(query, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/progress")
    public ResponseEntity<List<MonthlyExpenseDto>> getMonthlyExpense(
            @RequestParam Long period,
            @RequestHeader("Authorization") String header
    ){
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        return new ResponseEntity<>(expenseServiceImpl.getMonthlyProgress(userId,period),HttpStatus.OK);
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


    @GetMapping()
    public ResponseEntity<List<Expense>> getExpenseByUserId(
            @RequestHeader("Authorization") String Header
    ){
        boolean isValidToken= expenseServiceImpl.validateToken(Header);

        if(!isValidToken){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Long userId = expenseServiceImpl.extractUserId(Header);
        return new ResponseEntity<>(expenseServiceImpl.getExpensesByUserId(userId),HttpStatus.OK);
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

    @PatchMapping("/{expenseId}")
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
