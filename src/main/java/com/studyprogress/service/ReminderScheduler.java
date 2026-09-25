package com.studyprogress.service;

import com.studyprogress.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.reminders.enabled", havingValue = "true", matchIfMissing = true)
public class ReminderScheduler {
    private final ReminderRepository reminders;
    private final ReminderDeliveryService delivery;
    @Scheduled(fixedDelayString = "${app.reminders.interval-ms:60000}")
    public void dispatch() {
        reminders.findDue(Instant.now(), Instant.now().minusSeconds(300), PageRequest.of(0, 50)).forEach(delivery::deliver);
    }
}
