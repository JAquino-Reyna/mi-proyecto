package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record UserResponseDTO(Long id, String fullName, String email, User.Role role, boolean enabled) {}
