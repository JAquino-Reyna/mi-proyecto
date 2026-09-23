package com.studyprogress.service;

import com.studyprogress.dto.CategoryResponseDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        // TODO: Implementar la lógica o retornar una lista vacía por ahora
        return List.of();
    }
}