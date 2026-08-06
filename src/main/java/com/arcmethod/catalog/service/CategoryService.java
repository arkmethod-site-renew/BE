package com.arcmethod.catalog.service;
import com.arcmethod.catalog.dto.CategoryResponse;
import com.arcmethod.catalog.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll().stream().map(CategoryResponse::from).toList();
    }
}
