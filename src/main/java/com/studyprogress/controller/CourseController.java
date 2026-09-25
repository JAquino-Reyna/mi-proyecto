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
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService service;
    @GetMapping
    public Page<CourseResponseDTO> list(@RequestParam(defaultValue = "") String search, Pageable page) { return service.list(search, page); }
    @GetMapping("/{id}")
    public CourseDetailDTO get(@PathVariable Long id) { return service.get(id); }
    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@Valid @RequestBody CourseRequestDTO request) {
        var result = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/courses/" + result.id())).body(result);
    }
    @PutMapping("/{id}")
    public CourseResponseDTO update(@PathVariable Long id, @Valid @RequestBody CourseRequestDTO request) { return service.update(id, request); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
    @PutMapping("/{id}/share")
    public ShareResponseDTO share(@PathVariable Long id, @Valid @RequestBody ShareRequestDTO request) { return service.share(id, request); }
    @PostMapping("/{id}/collaborators")
    public ResponseEntity<CourseResponseDTO> add(@PathVariable Long id, @Valid @RequestBody CollaboratorRequestDTO request) {
        return ResponseEntity.status(201).body(service.addCollaborator(id, request));
    }
    @DeleteMapping("/{id}/collaborators/{userId}")
    public ResponseEntity<Void> remove(@PathVariable Long id, @PathVariable Long userId) {
        service.removeCollaborator(id, userId); return ResponseEntity.noContent().build();
    }
}
