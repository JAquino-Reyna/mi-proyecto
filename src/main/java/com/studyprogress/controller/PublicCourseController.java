package com.studyprogress.controller;

import com.studyprogress.dto.*;
import com.studyprogress.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/courses")
public class PublicCourseController {
    private final CourseService service;
    @GetMapping("/{code}")
    public PublicCourseDTO get(@PathVariable String code) { return service.publicCourse(code); }
}
