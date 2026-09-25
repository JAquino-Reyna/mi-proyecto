package com.studyprogress.repository;

import com.studyprogress.model.Course;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.*;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCategoryId(Long id);
    Optional<Course> findByShareCodeAndPublicAccessTrue(String code);
    @Query("select distinct c from Course c left join c.collaborators m where (c.user.id = :userId or m.id = :userId) and lower(c.title) like lower(concat('%', :search, '%'))")
    Page<Course> findAccessible(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);
    @Query("select distinct c from Course c left join c.collaborators m where c.user.id = :userId or m.id = :userId")
    List<Course> findAccessible(@Param("userId") Long userId);
    List<Course> findByUserId(Long userId);
}
