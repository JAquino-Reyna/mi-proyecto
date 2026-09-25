package com.studyprogress;

import com.studyprogress.config.JwtUtils;
import com.studyprogress.model.User;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTests {
    private static final String SECRET = "test-only-signing-key-for-studyprogress-token-tests-2026";
    private User user() {
        User user = new User();
        user.setId(7L);
        user.setEmail("student@example.com");
        return user;
    }
    @Test void tokenContainsIdentityAndRole() {
        JwtUtils jwt = new JwtUtils(SECRET, 900);
        var claims = jwt.claims(jwt.generateToken(user()));
        assertEquals("student@example.com", claims.getSubject());
        assertEquals("student@example.com", claims.get("email"));
        assertEquals(7, ((Number) claims.get("userId")).intValue());
        assertTrue(claims.get("roles").toString().contains("ROLE_STUDENT"));
    }
    @Test void expiredTokenIsRejected() {
        JwtUtils jwt = new JwtUtils(SECRET, -1);
        String token = jwt.generateToken(user());
        assertThrows(ExpiredJwtException.class, () -> jwt.claims(token));
    }
    @Test void tokenSignedByAnotherKeyIsRejected() {
        JwtUtils jwt = new JwtUtils(SECRET, 900);
        String token = new JwtUtils("another-test-only-key-that-is-at-least-thirty-two-bytes", 900).generateToken(user());
        assertThrows(JwtException.class, () -> jwt.claims(token));
    }
}
