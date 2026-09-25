package com.studyprogress.repository;

import com.studyprogress.model.Reminder;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.*;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    Page<Reminder> findByUserId(Long userId, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reminder r where r.id = :id")
    Optional<Reminder> findLocked(@Param("id") Long id);
    @Query("select r.id from Reminder r where r.sent = false and r.attempts < 3 and r.reminderTime <= :now and (r.lastAttempt is null or r.lastAttempt < :retry)")
    List<Long> findDue(@Param("now") Instant now, @Param("retry") Instant retry, Pageable pageable);
}
