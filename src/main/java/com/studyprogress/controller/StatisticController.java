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
@RequestMapping("/api/v1/statistics")
public class StatisticController {
    private final StatisticService service;
    @GetMapping("/dashboard")
    public DashboardResponseDTO dashboard() { return service.dashboard(); }
    @GetMapping
    public Page<StatisticResponseDTO> history(Pageable page) { return service.history(page); }
}
