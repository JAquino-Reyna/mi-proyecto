package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record CourseRequestDTO(@NotBlank @Size(max = 150) String title, @Size(max = 2000) String description, @NotNull @Positive Long categoryId) {}
