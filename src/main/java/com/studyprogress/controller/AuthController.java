package com.studyprogress.controller;

import com.studyprogress.config.JwtUtils;
import com.studyprogress.dto.AuthResponseDTO;
import com.studyprogress.dto.LoginRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        // Genera un token JWT de prueba si las credenciales son recibidas correctamente
        String token = jwtUtils.generateToken(loginRequest.getEmail(), "ROLE_STUDENT");
        return ResponseEntity.ok(new AuthResponseDTO(token, "Bearer", loginRequest.getEmail()));
    }
}