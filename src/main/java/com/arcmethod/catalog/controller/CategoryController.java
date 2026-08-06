package com.arcmethod.catalog.controller;

import com.arcmethod.catalog.dto.CategoryResponse;
import com.arcmethod.catalog.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    public List<CategoryResponse> list(){
        return categoryService.findAll();
    }
}
