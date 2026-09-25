package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record ReminderRequestDTO(@NotNull @Future Instant reminderTime, @NotEmpty @Size(max = 100) Set<@NotNull @Positive Long> taskIds) {}
