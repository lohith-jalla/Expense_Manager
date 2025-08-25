package com.lohith.Expense.Dto;

import com.lohith.Expense.Model.ExpenseType;
import com.lohith.Expense.Model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchUpdateDto {
    private String name;
    private Double amount;
    private String description;
    private ExpenseType type;
    private PaymentType paymentType;
    private LocalDate date;
}
