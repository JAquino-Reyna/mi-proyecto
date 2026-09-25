package com.studyprogress.service;

import com.studyprogress.dto.*;
import org.springframework.data.domain.*;
public interface CategoryService {
    Page<CategoryResponseDTO> list(Pageable page);
    CategoryResponseDTO get(Long id);
    CategoryResponseDTO create(CategoryRequestDTO request);
    CategoryResponseDTO update(Long id, CategoryRequestDTO request);
    void delete(Long id);
}
