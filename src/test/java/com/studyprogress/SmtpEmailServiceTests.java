package com.studyprogress;

import com.studyprogress.service.SmtpEmailService;
import com.studyprogress.exception.EmailDeliveryException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailSendException;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmtpEmailServiceTests {
    @Test void htmlTemplateEscapesUntrustedTextAndSendsMimeMessage() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);
        MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));
        when(sender.createMimeMessage()).thenReturn(message);
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        SmtpEmailService service = new SmtpEmailService(sender, engine);
        ReflectionTestUtils.setField(service, "from", "studyprogress@example.com");
        service.send("student@example.com", "Bienvenido", "<script>alert(1)</script>");
        verify(sender).send(message);
        assertEquals("Bienvenido", message.getSubject());
        assertFalse(message.getContent().toString().contains("<script>"));
        assertTrue(message.getContent().toString().contains("&lt;script&gt;"));
        doThrow(new MailSendException("offline")).when(sender).send(message);
        assertThrows(EmailDeliveryException.class, () -> service.send("student@example.com", "Prueba", "Mensaje"));
    }
}

