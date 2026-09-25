package com.studyprogress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version private Long version;
    @Column(nullable = false, length = 100) @NotBlank private String fullName;
    @Column(nullable = false, unique = true, length = 254) @NotBlank @Email private String email;
    @Column(nullable = false) @NotBlank private String password;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role = Role.ROLE_STUDENT;
    @Column(nullable = false) private boolean enabled = true;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) private List<Course> createdCourses = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) private List<Statistic> statistics = new ArrayList<>();
    public enum Role { ROLE_STUDENT, ROLE_ADMIN }
}
