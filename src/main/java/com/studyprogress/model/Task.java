package com.studyprogress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "tasks", indexes = @Index(name = "idx_tasks_lookup", columnList = "topic_id"))
@Getter
@Setter
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version private Long version;
    @Column(nullable = false, length = 150) @NotBlank private String title;
    @Column(length = 2000) private String description;
    private Instant dueDate;
    @Column(nullable = false) private boolean completed;
    @Column(nullable = false) @Min(0) private int studyMinutes;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "topic_id", nullable = false) private Topic topic;
    @ManyToMany(mappedBy = "tasks", fetch = FetchType.LAZY) private Set<Reminder> reminders = new HashSet<>();
}
