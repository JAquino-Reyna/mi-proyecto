package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record PublicCourseDTO(String title, String description, double progressPercentage, List<PublicTopicDTO> topics) {}
