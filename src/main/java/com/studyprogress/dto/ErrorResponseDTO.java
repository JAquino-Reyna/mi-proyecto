package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record ErrorResponseDTO(Instant timestamp, int status, String error, String message, String path, Map<String, String> details) {}
