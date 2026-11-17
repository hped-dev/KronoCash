package com.GIU26B.KronoCash.controller;

import com.GIU26B.KronoCash.model.Habit;
import com.GIU26B.KronoCash.repository.HabitRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/habits")
public class HabitController {

    private static final Long DEFAULT_USER_ID = 1L;

    private final HabitRepository habitRepository;

    public HabitController(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<Habit> habits = habitRepository.findByUserIdOrderByTitleAsc(DEFAULT_USER_ID);
        long completed = habits.stream().filter(Habit::isCompletedToday).count();

        model.addAttribute("userId", DEFAULT_USER_ID);
        model.addAttribute("habits", habits);
        model.addAttribute("completedToday", completed);
        model.addAttribute("totalToday", habits.size());
        model.addAttribute("habit", new Habit());

        return "public/habits";
    }

    @GetMapping("/new")
    public String newForm() {
        return "redirect:/habits";
    }

    @PostMapping
    public String save(@ModelAttribute("habit") @Valid Habit habit,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "public/habits";
        }
        habit.setUserId(DEFAULT_USER_ID);
        habit.setCompletedToday(false);
        habit.setStreakCount(0);
        habitRepository.save(habit);
        redirectAttributes.addFlashAttribute("message", "Hábito registrado correctamente.");
        return "redirect:/habits";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        habitRepository.findById(id).ifPresent(habit -> {
            boolean newState = !habit.isCompletedToday();
            habit.setCompletedToday(newState);
            if (newState) {
                habit.setStreakCount(habit.getStreakCount() + 1);
            } else {
                habit.setStreakCount(Math.max(0, habit.getStreakCount() - 1));
            }
            habitRepository.save(habit);
        });
        redirectAttributes.addFlashAttribute("message", "Estado del hábito actualizado.");
        return "redirect:/habits";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        habitRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Hábito eliminado correctamente.");
        return "redirect:/habits";
    }
}
