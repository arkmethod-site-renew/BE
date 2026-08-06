package com.arcmethod.catalog.controller;

import com.arcmethod.catalog.dto.ProductDetailResponse;
import com.arcmethod.catalog.dto.ProductSummaryResponse;
import com.arcmethod.catalog.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // GET /api/products              전체
    // GET /api/products?flag=new     신상
    // GET /api/products?flag=best    베스트
    // GET /api/products?category=top 카테고리
    @GetMapping
    public List<ProductSummaryResponse> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String flag) {
        return productService.getList(category, flag);
    }

    // GET /api/products/{slug}
    @GetMapping("/{slug}")
    public ProductDetailResponse detail(@PathVariable String slug) {
        return productService.getDetail(slug);
    }
}