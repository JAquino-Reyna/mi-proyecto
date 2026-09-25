package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record AuthResponseDTO(String token, String tokenType, String refreshToken, long expiresIn, UserResponseDTO user) {}
