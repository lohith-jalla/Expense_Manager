package com.lohith.Expense.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RecurringExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private ExpenseType type;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private FrequencyType frequency;

    @Enumerated(EnumType.STRING)
    private RecurringStatus status;
    private LocalDate startDate;
    private Long userId;
}
