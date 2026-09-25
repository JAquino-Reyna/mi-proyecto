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
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;
    @GetMapping("/me")
    public UserResponseDTO me() { return service.me(); }
    @PutMapping("/me")
    public UserResponseDTO update(@Valid @RequestBody UserUpdateDTO request) { return service.update(request); }
    @GetMapping
    public Page<UserResponseDTO> list(Pageable page) { return service.list(page); }
    @PatchMapping("/{id}/role")
    public UserResponseDTO role(@PathVariable Long id, @Valid @RequestBody UserRoleDTO request) { return service.role(id, request); }
}
