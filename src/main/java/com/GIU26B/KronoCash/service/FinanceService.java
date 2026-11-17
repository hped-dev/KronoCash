package com.GIU26B.KronoCash.service;

import com.GIU26B.KronoCash.model.SavingsGoal;
import com.GIU26B.KronoCash.repository.SavingsGoalRepository;
import com.GIU26B.KronoCash.repository.TransactionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FinanceService {

    private final TransactionRepository transactionRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public FinanceService(TransactionRepository transactionRepository,
                          SavingsGoalRepository savingsGoalRepository) {
        this.transactionRepository = transactionRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    /**
     * Calcula el balance total (ingresos - egresos) de un usuario.
     */
    public BigDecimal calculateTotalBalance(Long userId) {
        BigDecimal income = transactionRepository.sumIncomeByUser(userId);
        BigDecimal expense = transactionRepository.sumExpenseByUser(userId);
        return income.subtract(expense).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Devuelve ingresos y egresos del mes indicado para un usuario.
     */
    public MonthlySummary incomeVsExpenseByMonth(Long userId, int year, int month) {
        BigDecimal income = transactionRepository.sumIncomeByMonth(userId, year, month);
        BigDecimal expense = transactionRepository.sumExpenseByMonth(userId, year, month);
        return new MonthlySummary(year, month, income, expense);
    }

    /**
     * Devuelve todas las metas de ahorro de un usuario con su porcentaje de avance calculado.
     */
    public List<SavingsGoal> getSavingsGoalsWithProgress(Long userId) {
        List<SavingsGoal> goals = savingsGoalRepository.findByUserIdOrderByDeadlineAsc(userId);
        for (SavingsGoal goal : goals) {
            goal.getProgressPercentage();
        }
        return goals;
    }

    /**
     * Calcula el porcentaje de avance de una meta individual (0 a 100).
     */
    public BigDecimal calculateGoalProgress(SavingsGoal goal) {
        return goal.getProgressPercentage();
    }

    /**
     * Lista de categorías disponibles de un usuario para filtros.
     */
    public List<String> getCategories(Long userId) {
        return transactionRepository.findDistinctCategoriesByUser(userId);
    }

    /**
     * Rango del mes actual (primer día 00:00 a último día 23:59:59).
     */
    public MonthRange currentMonthRange() {
        YearMonth ym = YearMonth.now();
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);
        return new MonthRange(start, end);
    }

    /* --------------------------------------------------------------- */
    // Clases de soporte para la vista
    /* --------------------------------------------------------------- */

    public record MonthlySummary(int year, int month, BigDecimal income, BigDecimal expense) {
        public BigDecimal net() {
            return income.subtract(expense).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public record MonthRange(LocalDateTime start, LocalDateTime end) {
    }
}
