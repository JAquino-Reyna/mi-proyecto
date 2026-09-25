package com.studyprogress.event;

import com.studyprogress.service.EmailService;
import com.studyprogress.exception.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final EmailService email;
    @Async
    @TransactionalEventListener
    public void registered(UserRegisteredEvent event) {
        deliver(event.getEmail(), "Bienvenido a StudyProgress", "Hola " + event.getFullName() + ", tu cuenta está lista. Ya puedes organizar tus cursos.");
    }
    @Async
    @TransactionalEventListener
    public void shared(CourseSharedEvent event) {
        deliver(event.getEmail(), "Nuevo curso compartido", "Ahora puedes colaborar en el curso " + event.getTitle() + ".");
    }
    private void deliver(String recipient, String title, String message) {
        try { email.send(recipient, title, message); }
        catch (EmailDeliveryException ex) { log.warn("notification_delivery_failed type={}", title); }
    }
}
