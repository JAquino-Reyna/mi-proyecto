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
@RequestMapping("/api/v1/reminders")
public class ReminderController {
    private final ReminderService service;
    @GetMapping
    public Page<ReminderResponseDTO> list(Pageable page) { return service.list(page); }
    @GetMapping("/{id}")
    public ReminderResponseDTO get(@PathVariable Long id) { return service.get(id); }
    @PostMapping
    public ResponseEntity<ReminderResponseDTO> create(@Valid @RequestBody ReminderRequestDTO request) {
        var result = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/reminders/" + result.id())).body(result);
    }
    @PutMapping("/{id}")
    public ReminderResponseDTO update(@PathVariable Long id, @Valid @RequestBody ReminderRequestDTO request) { return service.update(id, request); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
