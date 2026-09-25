package com.studyprogress.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "statistics", indexes = @Index(name = "idx_statistics_lookup", columnList = "user_id,recorded_at"))
@Getter
@Setter
public class Statistic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version private Long version;
    @Column(nullable = false) private int studyMinutes;
    @Column(nullable = false) private int tasksCompletedCount;
    @Column(nullable = false) private int totalTasks;
    @Column(nullable = false) private Instant recordedAt = Instant.now();
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private User user;
}
