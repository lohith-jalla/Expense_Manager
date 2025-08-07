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
public class RecurringExpenseRequestDto {
    private String name;
    private String description;
    private Double amount;
    private String frequency;
    private ExpenseType type;
    private LocalDate startDate;
}
