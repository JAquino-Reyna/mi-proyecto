package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record LoginRequestDTO(@NotBlank @Email String email, @NotBlank @Size(max = 72) String password) {}
