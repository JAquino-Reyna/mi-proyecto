package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record CourseResponseDTO(Long id, String title, String description, Long categoryId, Long ownerId, double progressPercentage, boolean publicAccess, List<Long> collaboratorIds) {}
