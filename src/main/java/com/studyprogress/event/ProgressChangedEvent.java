package com.studyprogress.event;

import org.springframework.context.ApplicationEvent;
import lombok.Getter;
@Getter
public class ProgressChangedEvent extends ApplicationEvent {
    private final Long userId;
    public ProgressChangedEvent(Object source, Long userId) { super(source); this.userId = userId; }
}
