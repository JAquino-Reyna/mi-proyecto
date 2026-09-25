package com.studyprogress.repository;

import com.studyprogress.model.Topic;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.*;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByCourseIdOrderByOrderIndexAscIdAsc(Long courseId);
}
