package com.studyprogress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "courses", indexes = @Index(name = "idx_courses_lookup", columnList = "user_id,category_id"))
@Getter
@Setter
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version private Long version;
    @Column(nullable = false, length = 150) @NotBlank private String title;
    @Column(length = 2000) private String description;
    @Column(nullable = false) private boolean publicAccess;
    @Column(unique = true, length = 36) private String shareCode;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "category_id", nullable = false) private Category category;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private User user;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true) @OrderBy("orderIndex ASC, id ASC") private List<Topic> topics = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY) @JoinTable(name = "user_courses", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id")) private Set<User> collaborators = new HashSet<>();
}
