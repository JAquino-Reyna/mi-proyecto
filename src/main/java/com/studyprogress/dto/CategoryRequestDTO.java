package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record CategoryRequestDTO(@NotBlank @Size(max = 80) String name, @Size(max = 500) String description) {}
