package com.lohith.Expense.Services.ServiceImpl;

import com.lohith.Expense.Dto.MonthlyExpenseDto;
import com.lohith.Expense.Dto.PatchUpdateDto;
import com.lohith.Expense.Exceptions.ResourceNotFoundException;
import com.lohith.Expense.Model.Expense;
import com.lohith.Expense.Model.ExpenseType;
import com.lohith.Expense.Model.RecurringExpense;
import com.lohith.Expense.Repo.ExpenseRepo;
import com.lohith.Expense.Repo.RecurringExpenseRepo;
import com.lohith.Expense.Services.ExpenseServices;
import com.lohith.Expense.Services.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseServices {

    private final JwtService jwtService;
    private final ExpenseRepo  expenseRepo;
    private final RecurringExpenseRepo recurringRepo;

    public boolean validateToken(String Header){

        if(Header==null || !Header.startsWith("Bearer ")){
            return false;
        }
        String token=Header.substring(7);
        boolean validToken=jwtService.validate(token);

        return validToken;
    }

    public Page<Expense> getExpensesByUserId(long id, Pageable pageable){
        return expenseRepo.findAllByUserId(id,pageable);
    }

    public Long extractUserId(String Header){
        String token =  Header.substring(7);
        return Long.parseLong(jwtService.extractAllClaims(token).get("userId",String.class));
    }

    public Expense getExpenseByUserIdAndExpenseId(Long userId, Long expenseId) {
        return expenseRepo.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found for user " + userId));
    }

    public Expense createExpense(Expense expense, Long userId) {
        expense.setUserId(userId);
        expense.setDate(LocalDate.now());
        return expenseRepo.save(expense);
    }

    public Expense updateExpense(PatchUpdateDto expense, Long userId, Long expenseId){
        Expense expenseToBeUpdated=getExpenseByUserIdAndExpenseId(userId,expenseId);
        if(expenseToBeUpdated!=null && expenseToBeUpdated.getUserId()==userId){
            if(expense.getAmount()!=null) expenseToBeUpdated.setAmount(expense.getAmount());
            if(expense.getDescription()!=null) expenseToBeUpdated.setDescription(expense.getDescription());
            if(expense.getName()!=null) expenseToBeUpdated.setName(expense.getName());
            if(expense.getType()!=null) expenseToBeUpdated.setType(expense.getType());
            if(expense.getPaymentType()!=null) expenseToBeUpdated.setPaymentType(expense.getPaymentType());
            expenseRepo.save(expenseToBeUpdated);

            return expenseToBeUpdated;
        }
        return null;
    }

    public boolean deleteExpense(Long expenseId,Long userId){
        Expense expenseToBeDeleted=getExpenseByUserIdAndExpenseId(userId,expenseId);
        if(expenseToBeDeleted!=null){
            expenseRepo.delete(expenseToBeDeleted);
            return true;
        }
        return false;
    }

    // Get the Monthly Progress
    public Page<Object> getMonthlyProgress(Long userId, Long period, Pageable pageable){
        LocalDate today=LocalDate.now();
        LocalDate startDate=today.minusMonths(period);

        List<Expense> expenses = expenseRepo.findExpensesAfter(userId, startDate,pageable);

        // Group by month
        Map<YearMonth, Double> grouped = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> YearMonth.from(e.getDate()),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        // Convert to DTO
        List<Object>  resultDto = grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // sort by month
                .map(entry -> {
                    String monthName = entry.getKey().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                    String label = monthName + " " + entry.getKey().getYear();
                    return new MonthlyExpenseDto(label, entry.getValue());
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), resultDto.size());

        List<Object> pagedList = resultDto.subList(start, end);

        return new PageImpl<>(pagedList, pageable, resultDto.size());
    }

    // Get the summary of total expense by type.
    public Map<ExpenseType, Double> getExpenseSummaryByType(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = expenseRepo.getExpenseSummaryByType(userId, startDate, endDate);

        Map<ExpenseType, Double> summary = new HashMap<>();
        for (Object[] row : results) {
            ExpenseType type = (ExpenseType) row[0];
            Double total = (Double) row[1];
            summary.put(type, total);
        }
        return summary;
    }

    // Get monthly Expense
    public Map<String, Double> getMonthlySummary(Long userId, int year) {
        List<Object[]> results = expenseRepo.getMonthlySummary(userId, year);

        Map<String, Double> summary = new LinkedHashMap<>();
        for (Object[] row : results) {
            String month = (String) row[0];
            Double total = (Double) row[1];
            summary.put(month, total);
        }
        return summary;
    }

    public Map<String, Double> getWeeklySummary(Long userId, int year) {
        List<Object[]> results = expenseRepo.getWeeklySummary(userId, year);

        Map<String, Double> summary = new LinkedHashMap<>();
        for (Object[] row : results) {
            Integer week = ((Number) row[0]).intValue();
            Double total = (Double) row[1];
            summary.put("Week " + week, total);
        }
        return summary;
    }


    public Page<Object> searchExpenses(String query, Long userId, Pageable pageable) {
        List<Object> allExpenses = new ArrayList<>();

        // Exact name match
        List<Expense> expenses = expenseRepo.findByNameIgnoreCaseAndUserId(query, userId);
        List<RecurringExpense> recurringExpenses = recurringRepo.findByNameIgnoreCaseAndUserId(query, userId);

        allExpenses.addAll(expenses);
        allExpenses.addAll(recurringExpenses);

        // Partial name match
        expenses = (List<Expense>) expenseRepo.findByNameIgnoreCaseContainingAndUserId(query, userId);
        recurringExpenses = (List<RecurringExpense>) recurringRepo.findByNameIgnoreCaseContainingAndUserId(query, userId);

        allExpenses.addAll(expenses);
        allExpenses.addAll(recurringExpenses);

        // Type match
        boolean isValid = EnumUtils.isValidEnum(ExpenseType.class, query.toUpperCase());
        if (isValid) {
            allExpenses.addAll(searchExpensesByType(ExpenseType.valueOf(query.toUpperCase()), userId));
        }

        // 🧠 Remove duplicates (optional but recommended)
        List<Object> distinctExpenses = allExpenses.stream()
                .distinct()
                .collect(Collectors.toList());

        // 🧮 Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), distinctExpenses.size());

        List<Object> pagedList = distinctExpenses.subList(start, end);

        return new PageImpl<>(pagedList, pageable, distinctExpenses.size());
    }

    @Override
    public Collection<Object> searchExpensesByType(ExpenseType type, Long userId) {
        return expenseRepo.findAllByTypeAndUserId(type,userId);
    }


}
