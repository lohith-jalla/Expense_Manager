package com.lohith.Expense.Dto;

import com.lohith.Expense.Model.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecurringExpenseDto {
    private String name;
    private Double amount;
    private String description;
    private ExpenseType type;
    private String frequency;
    private LocalDate startDate;
    private Long userId;
}
