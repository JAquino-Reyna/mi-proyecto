package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record TaskProgressDTO(boolean completed, @Min(0) @Max(100000) int studyMinutes) {}
