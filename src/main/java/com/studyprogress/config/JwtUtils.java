package com.studyprogress.config;

import com.studyprogress.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.*;

@Component
public class JwtUtils {
    private final Key key;
    private final long expirationSeconds;
    public JwtUtils(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-seconds:900}") long expirationSeconds) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
    }
    public String generateToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder().setSubject(user.getEmail()).setIssuer("studyprogress")
                .claim("email", user.getEmail()).claim("userId", user.getId()).claim("roles", List.of(user.getRole().name()))
                .setIssuedAt(Date.from(now)).setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }
    public Claims claims(String token) {
        return Jwts.parserBuilder().requireIssuer("studyprogress").setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    public long expirationSeconds() { return expirationSeconds; }
}
