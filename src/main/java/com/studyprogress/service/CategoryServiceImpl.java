package com.studyprogress.service;

import com.studyprogress.dto.*;
import com.studyprogress.model.Category;
import com.studyprogress.repository.*;
import com.studyprogress.mapper.StudyMapper;
import com.studyprogress.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categories;
    private final CourseRepository courses;
    private final StudyMapper mapper;
    public Page<CategoryResponseDTO> list(Pageable page) { return categories.findAll(page).map(mapper::category); }
    public CategoryResponseDTO get(Long id) { return mapper.category(find(id)); }
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDTO create(CategoryRequestDTO request) {
        if (categories.existsByNameIgnoreCase(request.name().trim())) throw new DuplicateResourceException("La categoría ya existe");
        Category category = new Category();
        category.setName(request.name().trim());
        category.setDescription(request.description());
        return mapper.category(categories.save(category));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDTO update(Long id, CategoryRequestDTO request) {
        Category category = find(id);
        if (!category.getName().equalsIgnoreCase(request.name().trim()) && categories.existsByNameIgnoreCase(request.name().trim())) throw new DuplicateResourceException("La categoría ya existe");
        category.setName(request.name().trim());
        category.setDescription(request.description());
        return mapper.category(category);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Category category = find(id);
        if (courses.existsByCategoryId(id)) throw new CategoryInUseException("La categoría tiene cursos asociados");
        categories.delete(category);
    }
    private Category find(Long id) { return categories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada")); }
}
