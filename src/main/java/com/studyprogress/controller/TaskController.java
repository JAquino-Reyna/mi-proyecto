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
@RequestMapping("/api/v1")
public class TaskController {
    private final TaskService service;
    @GetMapping("/topics/{parentId}/tasks")
    public List<TaskResponseDTO> list(@PathVariable Long parentId) { return service.list(parentId); }
    @GetMapping("/tasks/{id}")
    public TaskResponseDTO get(@PathVariable Long id) { return service.get(id); }
    @PostMapping("/topics/{parentId}/tasks")
    public ResponseEntity<TaskResponseDTO> create(@PathVariable Long parentId, @Valid @RequestBody TaskRequestDTO request) {
        var result = service.create(parentId, request);
        return ResponseEntity.created(URI.create("/api/v1/tasks/" + result.id())).body(result);
    }
    @PutMapping("/tasks/{id}")
    public TaskResponseDTO update(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO request) { return service.update(id, request); }
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
    @PatchMapping("/tasks/{id}/progress")
    public TaskResponseDTO progress(@PathVariable Long id, @Valid @RequestBody TaskProgressDTO request) { return service.updateProgress(id, request); }
}
