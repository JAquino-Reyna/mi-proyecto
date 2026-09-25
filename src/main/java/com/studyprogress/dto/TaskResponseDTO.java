package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record TaskResponseDTO(Long id, Long topicId, String title, String description, Instant dueDate, boolean completed, int studyMinutes) {}
