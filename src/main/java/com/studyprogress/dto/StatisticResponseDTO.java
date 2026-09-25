package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record StatisticResponseDTO(Long id, Instant recordedAt, int totalTasks, int tasksCompletedCount, int studyMinutes) {}
