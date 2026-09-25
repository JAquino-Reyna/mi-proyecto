package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record UserUpdateDTO(@NotBlank @Size(min = 3, max = 100) String fullName) {}
