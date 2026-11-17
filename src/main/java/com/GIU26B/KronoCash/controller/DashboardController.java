package com.GIU26B.KronoCash.controller;

import com.GIU26B.KronoCash.model.SavingsGoal;
import com.GIU26B.KronoCash.service.FinanceService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Long DEFAULT_USER_ID = 1L;

    private final FinanceService financeService;

    public DashboardController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping
    public String dashboard(Model model) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        BigDecimal totalBalance = financeService.calculateTotalBalance(DEFAULT_USER_ID);
        FinanceService.MonthlySummary monthly = financeService.incomeVsExpenseByMonth(DEFAULT_USER_ID, year, month);
        List<SavingsGoal> goals = financeService.getSavingsGoalsWithProgress(DEFAULT_USER_ID);

        BigDecimal totalTarget = goals.stream()
                .map(SavingsGoal::getTargetAmount)
                .filter(b -> b != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCurrent = goals.stream()
                .map(SavingsGoal::getCurrentAmount)
                .filter(b -> b != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("userId", DEFAULT_USER_ID);
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("monthlyIncome", monthly.income());
        model.addAttribute("monthlyExpense", monthly.expense());
        model.addAttribute("monthlyNet", monthly.net());
        model.addAttribute("currentMonth", Month.of(month).name());
        model.addAttribute("currentYear", year);
        model.addAttribute("goals", goals);
        model.addAttribute("goalsCount", goals.size());
        model.addAttribute("goalsTotalTarget", totalTarget);
        model.addAttribute("goalsTotalCurrent", totalCurrent);

        return "public/dashboard";
    }
}
