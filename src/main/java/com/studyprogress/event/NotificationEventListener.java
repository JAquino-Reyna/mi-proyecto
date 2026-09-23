package com.studyprogress.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    @Async
    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        System.out.println("[ASYNC EVENT] Enviando correo de bienvenida a: " + event.getEmail());
    }
}