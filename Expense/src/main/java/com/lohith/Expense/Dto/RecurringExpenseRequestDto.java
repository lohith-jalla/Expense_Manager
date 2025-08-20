package com.lohith.Expense.Dto;


import com.lohith.Expense.Model.ExpenseType;
import com.lohith.Expense.Model.FrequencyType;
import com.lohith.Expense.Model.PaymentType;
import com.lohith.Expense.Model.RecurringStatus;
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
    private FrequencyType frequency;
    private ExpenseType type;
    private PaymentType paymentType;
    private RecurringStatus status;
    private LocalDate startDate;
}
