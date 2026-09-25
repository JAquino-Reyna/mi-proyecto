package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record TaskRequestDTO(@NotBlank @Size(max = 150) String title, @Size(max = 2000) String description, Instant dueDate) {}
