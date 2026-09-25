package com.studyprogress.config;

import com.studyprogress.model.*;
import com.studyprogress.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class BootstrapConfig implements CommandLineRunner {
    private final UserRepository users;
    private final CategoryRepository categories;
    private final PasswordEncoder encoder;
    @Value("${app.admin.email:}") private String adminEmail;
    @Value("${app.admin.password:}") private String adminPassword;
    @Override
    @Transactional
    public void run(String... args) {
        if (categories.count() == 0) {
            for (String name : new String[]{"Programación", "Matemáticas", "Idiomas", "Ciencias"}) {
                Category category = new Category();
                category.setName(name);
                categories.save(category);
            }
        }
        if (adminEmail.isBlank() || users.existsByEmail(adminEmail.toLowerCase(Locale.ROOT))) return;
        if (adminPassword.length() < 12 || adminPassword.length() > 72) throw new IllegalStateException("ADMIN_PASSWORD debe tener entre 12 y 72 caracteres");
        User admin = new User();
        admin.setEmail(adminEmail.toLowerCase(Locale.ROOT));
        admin.setFullName("Administrador");
        admin.setPassword(encoder.encode(adminPassword));
        admin.setRole(User.Role.ROLE_ADMIN);
        users.save(admin);
    }
}
