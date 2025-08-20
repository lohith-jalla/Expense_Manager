package com.lohith.Expense.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
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

    private LocalDate date;
    private Long userId;
}
