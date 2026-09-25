package com.studyprogress.repository;

import com.studyprogress.model.Statistic;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.*;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Page<Statistic> findByUserIdOrderByRecordedAtDesc(Long userId, Pageable pageable);
}
