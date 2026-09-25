package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record ReminderResponseDTO(Long id, Instant reminderTime, boolean sent, int attempts, Set<Long> taskIds) {}
