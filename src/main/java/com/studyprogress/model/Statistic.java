package com.studyprogress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "Los minutos de estudio no pueden ser negativos")
    private Integer studyMinutes = 0;

    @Min(value = 0, message = "La cantidad de tareas completadas no puede ser negativa")
    private Integer tasksCompletedCount = 0;

    @NotNull(message = "La fecha de registro es obligatoria")
    private LocalDate recordedAt = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}