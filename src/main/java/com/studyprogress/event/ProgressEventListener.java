package com.studyprogress.event;

import com.studyprogress.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProgressEventListener {
    private final StatisticService statistics;
    @Async
    @TransactionalEventListener
    public void changed(ProgressChangedEvent event) { statistics.snapshot(event.getUserId()); }
}
