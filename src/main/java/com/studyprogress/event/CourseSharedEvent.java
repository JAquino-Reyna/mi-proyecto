package com.studyprogress.event;

import org.springframework.context.ApplicationEvent;
import lombok.Getter;
@Getter
public class CourseSharedEvent extends ApplicationEvent {
    private final String email;
    private final String title;
    public CourseSharedEvent(Object source, String email, String title) { super(source); this.email = email; this.title = title; }
}
