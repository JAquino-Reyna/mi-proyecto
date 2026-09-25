package com.studyprogress.repository;

import com.studyprogress.model.Task;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.*;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTopicId(Long topicId);
    @Query("select t from Task t where t.topic.course.user.id = :userId")
    List<Task> findOwned(@Param("userId") Long userId);
}
