package com.lohith.Expense.Dto;

import com.lohith.Expense.Model.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchUpdateDto {
    private String name;
    private Double amount;
    private String description;
    private ExpenseType type;
}
