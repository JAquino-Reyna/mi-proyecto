package com.studyprogress.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "reminders", indexes = @Index(name = "idx_reminders_lookup", columnList = "user_id,reminder_time"))
@Getter
@Setter
public class Reminder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version private Long version;
    @Column(nullable = false) private Instant reminderTime;
    @Column(nullable = false) private boolean sent;
    @Column(nullable = false) private int attempts;
    private Instant lastAttempt;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private User user;
    @ManyToMany(fetch = FetchType.LAZY) @JoinTable(name = "task_reminders", joinColumns = @JoinColumn(name = "reminder_id"), inverseJoinColumns = @JoinColumn(name = "task_id")) private Set<Task> tasks = new HashSet<>();
}
