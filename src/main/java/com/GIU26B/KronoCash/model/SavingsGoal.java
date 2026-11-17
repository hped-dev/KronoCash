package com.GIU26B.KronoCash.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "savings_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "El título de la meta es obligatorio")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotNull(message = "El monto objetivo es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto objetivo debe ser mayor a 0")
    @Column(name = "target_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal targetAmount;

    @NotNull(message = "El monto actual es obligatorio")
    @DecimalMin(value = "0", message = "El monto actual no puede ser negativo")
    @Column(name = "current_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentAmount;

    @NotNull(message = "La fecha límite es obligatoria")
    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    /**
     * Calcula el porcentaje de avance de la meta (0 a 100).
     */
    public BigDecimal getProgressPercentage() {
        if (targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal current = currentAmount != null ? currentAmount : BigDecimal.ZERO;
        BigDecimal ratio = current.divide(targetAmount, 4, RoundingMode.HALF_UP);
        return ratio.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Indica si la meta ya se alcanzó o superó.
     */
    public boolean isCompleted() {
        if (currentAmount == null || targetAmount == null) {
            return false;
        }
        return currentAmount.compareTo(targetAmount) >= 0;
    }
}
