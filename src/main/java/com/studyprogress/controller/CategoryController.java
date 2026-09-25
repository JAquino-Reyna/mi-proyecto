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
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService service;
    @GetMapping
    public Page<CategoryResponseDTO> list(Pageable page) { return service.list(page); }
    @GetMapping("/{id}")
    public CategoryResponseDTO get(@PathVariable Long id) { return service.get(id); }
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryRequestDTO request) {
        var result = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + result.id())).body(result);
    }
    @PutMapping("/{id}")
    public CategoryResponseDTO update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO request) { return service.update(id, request); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
