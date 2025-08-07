package com.lohith.Expense.Services;

import com.lohith.Expense.Dto.RecurringExpenseRequestDto;
import com.lohith.Expense.Model.RecurringExpense;

import java.util.List;

public interface RecurringExpenseServices {
     RecurringExpense create(Long userId, RecurringExpenseRequestDto dto);
     RecurringExpense update(Long expenseId,RecurringExpenseRequestDto expense);
     List<RecurringExpense> getAllForUser(Long userId);
     RecurringExpense getById(Long expenseId);
     boolean deleteWithPassword(Long recurringId, Long userId, String password , String token);

}
