package com.lohith.Expense.Services;

import com.lohith.Expense.Dto.MonthlyExpenseDto;
import com.lohith.Expense.Dto.PatchUpdateDto;
import com.lohith.Expense.Model.Expense;
import com.lohith.Expense.Model.ExpenseType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExpenseServices {
     boolean validateToken(String Header);
     List<Expense> getExpensesByUserId(long id);
     Expense getExpenseByUserIdAndExpenseId(Long userId, Long expenseId);
     Expense createExpense(Expense expense, Long userId);
     Expense updateExpense(PatchUpdateDto expense, Long userId, Long expenseId);
     boolean deleteExpense(Long expenseId,Long userId);
     List<MonthlyExpenseDto> getMonthlyProgress(Long userId, Long period);
     Map<ExpenseType, Double> getExpenseSummaryByType(Long userId, LocalDate startDate, LocalDate endDate);
     Map<String, Double> getMonthlySummary(Long userId, int year);
     List<Object> searchExpenses(String query, Long userId);
     List<Object> searchExpensesByType(ExpenseType type,Long userId);
}
