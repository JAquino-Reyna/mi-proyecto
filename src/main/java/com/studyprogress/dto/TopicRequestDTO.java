package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record TopicRequestDTO(@NotBlank @Size(max = 150) String title, @Min(0) int orderIndex) {}
