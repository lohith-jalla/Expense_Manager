package com.lohith.Expense.Repo;

import com.lohith.Expense.Model.ExpenseType;
import com.lohith.Expense.Model.RecurringExpense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecurringExpenseRepo extends JpaRepository<RecurringExpense,Long> {

    Page<RecurringExpense> findAllByUserId(Long userId, Pageable pageable);

    List<RecurringExpense> findByTypeAndUserId(ExpenseType type, Long userId);

    List<RecurringExpense> findByNameIgnoreCaseAndUserId(String name, Long userId);

    Collection<? extends RecurringExpense> findByNameIgnoreCaseContainingAndUserId(String name, Long userId);
}
