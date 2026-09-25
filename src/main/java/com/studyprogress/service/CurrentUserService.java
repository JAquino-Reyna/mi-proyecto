package com.studyprogress.service;

import com.studyprogress.model.User;
import com.studyprogress.repository.UserRepository;
import com.studyprogress.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository users;
    public User get() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) throw new UnauthorizedException("Inicia sesión para continuar");
        return users.findByEmail(authentication.getName()).filter(User::isEnabled)
                .orElseThrow(() -> new UnauthorizedException("Usuario no disponible"));
    }
}
