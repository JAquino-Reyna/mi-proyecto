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
public class TopicController {
    private final TopicService service;
    @GetMapping("/courses/{parentId}/topics")
    public List<TopicResponseDTO> list(@PathVariable Long parentId) { return service.list(parentId); }
    @GetMapping("/topics/{id}")
    public TopicResponseDTO get(@PathVariable Long id) { return service.get(id); }
    @PostMapping("/courses/{parentId}/topics")
    public ResponseEntity<TopicResponseDTO> create(@PathVariable Long parentId, @Valid @RequestBody TopicRequestDTO request) {
        var result = service.create(parentId, request);
        return ResponseEntity.created(URI.create("/api/v1/topics/" + result.id())).body(result);
    }
    @PutMapping("/topics/{id}")
    public TopicResponseDTO update(@PathVariable Long id, @Valid @RequestBody TopicRequestDTO request) { return service.update(id, request); }
    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
    @PatchMapping("/topics/{id}/progress")
    public TopicResponseDTO progress(@PathVariable Long id, @Valid @RequestBody TopicProgressDTO request) { return service.complete(id, request); }
}
