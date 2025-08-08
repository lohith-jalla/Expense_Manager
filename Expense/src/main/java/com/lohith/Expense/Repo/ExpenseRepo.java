package com.lohith.Expense.Repo;

import com.lohith.Expense.Model.Expense;
import com.lohith.Expense.Model.ExpenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense,Long> {
    Page<Expense> findAllByUserId(Long userId ,Pageable pageable);
    Optional<Expense> findByIdAndUserId(Long expenseId, Long userId);
    Optional<Expense> findById(Long expenseId);

    @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND e.date >= :startDate")
    List<Expense> findExpensesAfter(Long userId, LocalDate startDate,Pageable pageable);

    @Query("SELECT e.type, SUM(e.amount) FROM Expense e " +
            "WHERE e.userId = :userId AND e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY e.type")
    List<Object[]> getExpenseSummaryByType(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("SELECT FUNCTION('DATE_FORMAT', e.date, '%Y-%m'), SUM(e.amount)\n" +
            "FROM Expense e \n" +
            "WHERE e.userId = :userId AND YEAR(e.date) = :year \n" +
            "GROUP BY FUNCTION('DATE_FORMAT', e.date, '%Y-%m') \n" +
            "ORDER BY FUNCTION('DATE_FORMAT', e.date, '%Y-%m')")
    List<Object[]> getMonthlySummary(Long userId, int year);

    List<Expense> findByNameIgnoreCaseAndUserId(String query, Long userId);


    List<Expense> findByTypeAndUserId(ExpenseType type, Long userId);

    Collection<? extends Expense> findByNameIgnoreCaseContainingAndUserId(String name, Long userId);

    Collection<Object> findAllByTypeAndUserId(ExpenseType type, Long userId);
}
