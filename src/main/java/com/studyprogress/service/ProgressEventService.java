package com.studyprogress.service;

import com.studyprogress.event.ProgressChangedEvent;
import com.studyprogress.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class ProgressEventService {
    private final ApplicationEventPublisher events;
    public void changed(Course course) { events.publishEvent(new ProgressChangedEvent(this, course.getUser().getId())); }
}
