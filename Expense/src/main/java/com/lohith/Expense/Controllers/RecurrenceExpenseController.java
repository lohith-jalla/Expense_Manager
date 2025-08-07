package com.lohith.Expense.Controllers;

import com.lohith.Expense.Dto.RecurringExpenseRequestDto;
import com.lohith.Expense.Model.RecurringExpense;
import com.lohith.Expense.Services.ServiceImpl.ExpenseServiceImpl;
import com.lohith.Expense.Services.ServiceImpl.RecurringExpenseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense/RExpense/")
@RequiredArgsConstructor
public class RecurrenceExpenseController {

    private final RecurringExpenseServiceImpl recurringExpenseServiceImpl;
    private final ExpenseServiceImpl expenseServiceImpl;


    @GetMapping("")
    public ResponseEntity<List<RecurringExpense>> getAllRecurringExpense(
            @RequestHeader("Authorization") String header
    ){
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        return new ResponseEntity<>(recurringExpenseServiceImpl.getAllForUser(userId),HttpStatus.OK);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<RecurringExpense> getRecurringExpenseById(
            @RequestHeader("Authorization") String header,
            @PathVariable("expenseId") Long expenseId
    ) {
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        RecurringExpense expense= recurringExpenseServiceImpl.getById(expenseId);
        if(expense==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(expense);

    }


    @PostMapping("")
    public ResponseEntity<RecurringExpense> addRecurringExpense(
            @RequestHeader("Authorization") String header,
            @RequestBody RecurringExpenseRequestDto expense){
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId= expenseServiceImpl.extractUserId(header);
        return new ResponseEntity<>(recurringExpenseServiceImpl.create(userId,expense),HttpStatus.CREATED);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<RecurringExpense> updateRecurringExpense(
            @RequestHeader("Authorization") String header,
            @PathVariable("expenseId") Long expenseId,
            @RequestBody RecurringExpenseRequestDto expense){
        if (!expenseServiceImpl.validateToken(header)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        RecurringExpense RExpense = recurringExpenseServiceImpl.update(expenseId,expense);
        if(RExpense!=null){
            return ResponseEntity.ok(RExpense);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpense(
            @RequestHeader("Authorization") String header,
            @RequestHeader("X-User-password") String password,
            @PathVariable("expenseId") Long expenseId
    ){
        if (!expenseServiceImpl.validateToken(header)) {
            return new ResponseEntity<>("Wrong user!!",HttpStatus.UNAUTHORIZED);
        }

        String token=header.substring(7);
        Long userId= expenseServiceImpl.extractUserId(header);
        boolean isDeleted= recurringExpenseServiceImpl.deleteWithPassword(expenseId,userId,password,token);

        if(isDeleted){
            return ResponseEntity.ok("Expense deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
