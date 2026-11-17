package com.GIU26B.KronoCash.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "habits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "El título del hábito es obligatorio")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "La frecuencia es obligatoria")
    @Column(name = "frequency", nullable = false, length = 100)
    private String frequency;

    @Column(name = "completed_today", nullable = false)
    private Boolean completedToday = false;

    @Column(name = "streak_count", nullable = false)
    private Integer streakCount = 0;

    public boolean isCompletedToday() {
        return Boolean.TRUE.equals(completedToday);
    }
}
