package com.studyprogress.service;

import com.studyprogress.model.*;
import com.studyprogress.repository.ReminderRepository;
import com.studyprogress.exception.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderDeliveryService {
    private final ReminderRepository reminders;
    private final EmailService email;
    @Async
    @Transactional
    public void deliver(Long id) {
        reminders.findLocked(id).ifPresent(reminder -> {
            if (reminder.isSent() || reminder.getAttempts() >= 3 || reminder.getReminderTime().isAfter(Instant.now())) return;
            if (reminder.getLastAttempt() != null && reminder.getLastAttempt().isAfter(Instant.now().minusSeconds(300))) return;
            var pending = reminder.getTasks().stream().filter(t -> !t.isCompleted() && hasAccess(reminder.getUser(), t.getTopic().getCourse())).toList();
            if (pending.isEmpty() || !reminder.getUser().isEnabled()) { reminder.setSent(true); return; }
            reminder.setAttempts(reminder.getAttempts() + 1);
            reminder.setLastAttempt(Instant.now());
            try {
                email.send(reminder.getUser().getEmail(), "Tareas pendientes", pending.stream().map(Task::getTitle).collect(Collectors.joining(", ")));
                reminder.setSent(true);
            } catch (EmailDeliveryException ex) { log.warn("reminder_delivery_failed id={} attempt={}", id, reminder.getAttempts()); }
        });
    }
    private boolean hasAccess(User user, Course course) {
        return course.getUser().getId().equals(user.getId()) || course.getCollaborators().stream().anyMatch(u -> u.getId().equals(user.getId()));
    }
}
