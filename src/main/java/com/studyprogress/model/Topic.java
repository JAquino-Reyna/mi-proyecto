package com.studyprogress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "topics", indexes = @Index(name = "idx_topics_lookup", columnList = "course_id"))
@Getter
@Setter
public class Topic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version private Long version;
    @Column(nullable = false, length = 150) @NotBlank private String title;
    @Column(nullable = false) private int orderIndex;
    @Column(nullable = false) private boolean completed;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "course_id", nullable = false) private Course course;
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true) @OrderBy("id ASC") private List<Task> tasks = new ArrayList<>();
}
