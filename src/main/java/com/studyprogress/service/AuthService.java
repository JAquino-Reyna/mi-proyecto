package com.studyprogress.service;

import com.studyprogress.config.JwtUtils;
import com.studyprogress.dto.*;
import com.studyprogress.event.UserRegisteredEvent;
import com.studyprogress.exception.*;
import com.studyprogress.mapper.StudyMapper;
import com.studyprogress.model.User;
import com.studyprogress.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtils jwt;
    private final RefreshTokenService refreshTokens;
    private final StudyMapper mapper;
    private final ApplicationEventPublisher events;
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (request.password().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) throw new InvalidOperationException("La contraseña excede 72 bytes");
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(email)) throw new DuplicateResourceException("El correo ya estÃ¡ registrado");
        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPassword(encoder.encode(request.password()));
        users.saveAndFlush(user);
        events.publishEvent(new UserRegisteredEvent(this, user.getEmail(), user.getFullName()));
        return authenticate(user);
    }
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = users.findByEmail(request.email().trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new UnauthorizedException("Credenciales incorrectas"));
        if (!user.isEnabled() || !encoder.matches(request.password(), user.getPassword())) throw new UnauthorizedException("Credenciales incorrectas");
        return authenticate(user);
    }
    public AuthResponseDTO refresh(RefreshRequestDTO request) { return authenticate(refreshTokens.consume(request.refreshToken())); }
    public void logout(RefreshRequestDTO request) { refreshTokens.consume(request.refreshToken()); }
    private AuthResponseDTO authenticate(User user) {
        return new AuthResponseDTO(jwt.generateToken(user), "Bearer", refreshTokens.issue(user), jwt.expirationSeconds(), mapper.user(user));
    }
}
