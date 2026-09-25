package com.studyprogress.service;

import com.studyprogress.dto.*;
import com.studyprogress.mapper.StudyMapper;
import com.studyprogress.exception.ResourceNotFoundException;
import com.studyprogress.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final CurrentUserService current;
    private final UserRepository users;
    private final StudyMapper mapper;
    public UserResponseDTO me() { return mapper.user(current.get()); }
    public UserResponseDTO update(UserUpdateDTO request) {
        var user = current.get();
        user.setFullName(request.fullName().trim());
        return mapper.user(user);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDTO> list(Pageable page) { return users.findAll(page).map(mapper::user); }
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO role(Long id, UserRoleDTO request) {
        var user = users.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        user.setRole(request.role());
        return mapper.user(user);
    }
}
