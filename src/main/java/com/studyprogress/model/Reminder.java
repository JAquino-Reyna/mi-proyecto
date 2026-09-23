package com.studyprogress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha y hora del recordatorio son obligatorias")
    private LocalDateTime reminderTime;

    private Boolean sent = false;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType = NotificationType.EMAIL;

    @ManyToMany(mappedBy = "reminders")
    private List<Task> tasks;

    public enum NotificationType {
        EMAIL,
        IN_APP
    }
}