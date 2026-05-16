package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.expensetracker.dto.CategorySpendRow;
import org.springframework.data.domain.Pageable;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByOccurredOnBetweenOrderByOccurredOnDesc(LocalDate start, LocalDate end);

    List<Transaction> findByOccurredOnBetweenAndCategory_IdOrderByOccurredOnDesc(LocalDate start, LocalDate end, Long categoryId);

    List<Transaction> findTop10ByOrderByOccurredOnDescIdDesc();

    long countByCategory_Id(Long categoryId);

    @Query("SELECT SUM(t.amount)" +
            " FROM Transaction t" +
            " WHERE t.occurredOn " +
            "BETWEEN :start AND :end " +
            "AND t.category.type = com.example.expensetracker.entity.CategoryType.EXPENSE")
    BigDecimal sumExpensesBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT new com.example.expensetracker.dto.CategorySpendRow(c.name, SUM(t.amount)) " +
            "FROM Transaction t " +
            "JOIN t.category c " +
            "WHERE t.occurredOn " +
            "BETWEEN :start AND :end " +
            "AND c.type = com.example.expensetracker.entity.CategoryType.EXPENSE " +
            "GROUP BY c.name " +
            "ORDER BY SUM(t.amount) DESC")
    List<CategorySpendRow> topExpenseCategoriesBetween(@Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);
}
