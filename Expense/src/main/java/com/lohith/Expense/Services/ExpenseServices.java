package com.lohith.Expense.Services;

import com.lohith.Expense.Dto.PatchUpdateDto;
import com.lohith.Expense.Model.Expense;
import com.lohith.Expense.Model.ExpenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public interface ExpenseServices {
     boolean validateToken(String Header);
     Page<Expense> getExpensesByUserId(long id, Pageable pageable);
     Expense getExpenseByUserIdAndExpenseId(Long userId, Long expenseId);
     Expense createExpense(Expense expense, Long userId);
     Expense updateExpense(PatchUpdateDto expense, Long userId, Long expenseId);
     boolean deleteExpense(Long expenseId,Long userId);
     Page<Object> getMonthlyProgress(Long userId, Long period, Pageable pageable);
     Map<ExpenseType, Double> getExpenseSummaryByType(Long userId, LocalDate startDate, LocalDate endDate);
     Map<String, Double> getMonthlySummary(Long userId, int year);
     Page<Object> searchExpenses(String query, Long userId,Pageable pageable);
     Collection<Object> searchExpensesByType(ExpenseType type, Long userId);
     Map<String, Double> getWeeklySummary(Long userId, int year);
}
