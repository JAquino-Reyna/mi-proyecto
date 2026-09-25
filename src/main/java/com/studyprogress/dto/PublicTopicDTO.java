package com.studyprogress.dto;

import jakarta.validation.constraints.*;
import com.studyprogress.model.User;
import java.time.Instant;
import java.util.*;

public record PublicTopicDTO(String title, boolean completed, List<PublicTaskDTO> tasks) {}
