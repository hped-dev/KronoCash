package com.GIU26B.KronoCash.controller;

import com.GIU26B.KronoCash.model.Transaction;
import com.GIU26B.KronoCash.repository.TransactionRepository;
import com.GIU26B.KronoCash.service.FinanceService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final DateTimeFormatter DATE_TIME_INPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final TransactionRepository transactionRepository;
    private final FinanceService financeService;

    public TransactionController(TransactionRepository transactionRepository,
                                 FinanceService financeService) {
        this.transactionRepository = transactionRepository;
        this.financeService = financeService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String category,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate) {

        List<Transaction> transactions;

        boolean hasFilter = category != null || type != null || startDate != null || endDate != null;

        if (hasFilter) {
            Transaction.Type t = (type != null && !type.isBlank())
                    ? Transaction.Type.valueOf(type.toUpperCase()) : null;

            if (category != null && !category.isBlank() && t != null) {
                transactions = transactionRepository.findByUserIdAndCategoryOrderByTransactionDateDesc(
                        DEFAULT_USER_ID, category);
                transactions.removeIf(tx -> tx.getType() != t);
            } else if (t != null) {
                transactions = transactionRepository.findByUserIdAndTypeOrderByTransactionDateDesc(DEFAULT_USER_ID, t);
            } else if (category != null && !category.isBlank()) {
                transactions = transactionRepository.findByUserIdAndCategoryOrderByTransactionDateDesc(
                        DEFAULT_USER_ID, category);
            } else {
                transactions = transactionRepository.findByUserIdOrderByTransactionDateDesc(DEFAULT_USER_ID);
            }

            if (startDate != null && !startDate.isBlank()) {
                LocalDateTime start = LocalDateTime.parse(startDate, DATE_TIME_INPUT);
                transactions.removeIf(tx -> tx.getTransactionDate().isBefore(start));
            }
            if (endDate != null && !endDate.isBlank()) {
                LocalDateTime end = LocalDateTime.parse(endDate, DATE_TIME_INPUT);
                transactions.removeIf(tx -> tx.getTransactionDate().isAfter(end));
            }
        } else {
            transactions = transactionRepository.findByUserIdOrderByTransactionDateDesc(DEFAULT_USER_ID);
        }

        BigDecimal balance = financeService.calculateTotalBalance(DEFAULT_USER_ID);
        List<String> categories = financeService.getCategories(DEFAULT_USER_ID);

        model.addAttribute("userId", DEFAULT_USER_ID);
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        model.addAttribute("totalBalance", balance);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedType", type);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("types", Transaction.Type.values());

        return "public/transactions";
    }

    @GetMapping("/new")
    public String newForm() {
        return "redirect:/transactions";
    }

    @PostMapping
    public String save(@ModelAttribute("transaction") @Valid Transaction transaction,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "public/transactions";
        }
        transaction.setUserId(DEFAULT_USER_ID);
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }
        transactionRepository.save(transaction);
        redirectAttributes.addFlashAttribute("message", "Movimiento registrado correctamente.");
        return "redirect:/transactions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        transactionRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Movimiento eliminado correctamente.");
        return "redirect:/transactions";
    }
}
