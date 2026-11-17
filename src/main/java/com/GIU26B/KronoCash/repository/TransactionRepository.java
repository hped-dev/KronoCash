package com.GIU26B.KronoCash.repository;

import com.GIU26B.KronoCash.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);

    List<Transaction> findByUserIdAndTypeOrderByTransactionDateDesc(Long userId, Transaction.Type type);

    List<Transaction> findByUserIdAndCategoryOrderByTransactionDateDesc(Long userId, String category);

    List<Transaction> findByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId AND t.type = 'INCOME'")
    BigDecimal sumIncomeByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId AND t.type = 'EXPENSE'")
    BigDecimal sumExpenseByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId AND t.type = 'INCOME' "
            + "AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    BigDecimal sumIncomeByMonth(@Param("userId") Long userId,
                                @Param("year") int year,
                                @Param("month") int month);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId AND t.type = 'EXPENSE' "
            + "AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    BigDecimal sumExpenseByMonth(@Param("userId") Long userId,
                                 @Param("year") int year,
                                 @Param("month") int month);

    @Query("SELECT DISTINCT t.category FROM Transaction t WHERE t.userId = :userId ORDER BY t.category")
    List<String> findDistinctCategoriesByUser(@Param("userId") Long userId);
}
