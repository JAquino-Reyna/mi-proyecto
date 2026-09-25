package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record RegisterRequestDTO(@NotBlank @Size(min = 3, max = 100) String fullName, @NotBlank @Email @Size(max = 254) String email, @NotBlank @Size(min = 8, max = 72) @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$", message = "Debe incluir mayúscula, minúscula y número") String password) {}
