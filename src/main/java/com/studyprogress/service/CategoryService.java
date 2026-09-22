package com.studyprogress.service;

import com.studyprogress.dto.CategoryResponseDTO;
import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
}