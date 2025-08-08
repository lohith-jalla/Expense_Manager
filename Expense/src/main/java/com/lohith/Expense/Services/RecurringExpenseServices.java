package com.lohith.Expense.Services;

import com.lohith.Expense.Dto.RecurringExpenseRequestDto;
import com.lohith.Expense.Model.RecurringExpense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RecurringExpenseServices {
     RecurringExpense create(Long userId, RecurringExpenseRequestDto dto);
     RecurringExpense update(Long expenseId,RecurringExpenseRequestDto expense);
     Page<RecurringExpense> getAllForUser(Long userId, Pageable pageable);
     RecurringExpense getById(Long expenseId);
     boolean deleteWithPassword(Long recurringId, Long userId, String password , String token);

}
