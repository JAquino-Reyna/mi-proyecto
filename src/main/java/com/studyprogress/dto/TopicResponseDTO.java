package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record TopicResponseDTO(Long id, Long courseId, String title, int orderIndex, boolean completed, List<TaskResponseDTO> tasks) {}
