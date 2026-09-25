package com.studyprogress.service;

import com.studyprogress.exception.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SmtpEmailService implements EmailService {
    private final JavaMailSender mail;
    private final TemplateEngine templates;
    @Value("${app.mail.from}") private String from;
    public void send(String recipient, String heading, String message) {
        try {
            Context context = new Context();
            context.setVariable("heading", heading);
            context.setVariable("message", message);
            var mime = mail.createMimeMessage();
            var helper = new MimeMessageHelper(mime, "UTF-8");
            helper.setFrom(from);
            helper.setTo(recipient);
            helper.setSubject(heading);
            helper.setText(templates.process("email", context), true);
            mail.send(mime);
        } catch (Exception ex) { throw new EmailDeliveryException("No se pudo entregar el correo"); }
    }
}
