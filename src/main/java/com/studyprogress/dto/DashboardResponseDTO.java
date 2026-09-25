package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record DashboardResponseDTO(int courses, long totalTasks, long completedTasks, long studyMinutes, double progressPercentage, List<CourseResponseDTO> courseProgress) {}
