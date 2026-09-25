package com.studyprogress.service;

import com.studyprogress.model.*;
import com.studyprogress.repository.RefreshTokenRepository;
import com.studyprogress.exception.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository tokens;
    public String issue(User user) {
        byte[] random = new byte[48];
        new SecureRandom().nextBytes(random);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        RefreshToken token = new RefreshToken();
        token.setTokenHash(hash(raw));
        token.setExpiresAt(Instant.now().plusSeconds(604800));
        token.setUser(user);
        tokens.save(token);
        return raw;
    }
    @Transactional
    public User consume(String raw) {
        RefreshToken token = tokens.findByHashForUpdate(hash(raw)).orElseThrow(() -> new InvalidRefreshTokenException("Refresh token inválido"));
        if (token.getExpiresAt().isBefore(Instant.now()) || !token.getUser().isEnabled()) throw new InvalidRefreshTokenException("Refresh token vencido o revocado");
        User user = token.getUser();
        tokens.delete(token);
        tokens.flush();
        return user;
    }
    private String hash(String raw) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException ex) { throw new IllegalStateException(ex); }
    }
}
