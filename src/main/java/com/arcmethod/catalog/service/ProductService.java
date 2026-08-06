package com.arcmethod.catalog.service;

import com.arcmethod.catalog.domain.Product;
import com.arcmethod.catalog.domain.ProductImage;
import com.arcmethod.catalog.domain.ProductVariant;
import com.arcmethod.catalog.domain.Promotion;
import com.arcmethod.catalog.dto.ProductDetailResponse;
import com.arcmethod.catalog.dto.ProductSummaryResponse;
import com.arcmethod.catalog.repository.ProductImageRepository;
import com.arcmethod.catalog.repository.ProductMeasurementRepository;
import com.arcmethod.catalog.repository.ProductRepository;
import com.arcmethod.catalog.repository.ProductVariantRepository;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductMeasurementRepository measurementRepository;
    private final PromotionService promotionService;

    public List<ProductSummaryResponse> getList(String category, String flag) {
        List<Product> products;
        if (category != null && !category.isBlank()) {
            products = productRepository.findByCategory_SlugOrderByCreatedAtDesc(category);
        } else if ("new".equalsIgnoreCase(flag)) {
            products = productRepository.findByIsNewTrueOrderByCreatedAtDesc();
        } else if ("best".equalsIgnoreCase(flag)) {
            products = productRepository.findByIsBestTrueOrderByCreatedAtDesc();
        } else {
            products = productRepository.findAll();
        }

        List<Long> ids = products.stream().map(Product::getId).toList();
        if (ids.isEmpty()) {
            return List.of();
        }

        Map<Long, List<ProductImage>> imagesByProduct = imageRepository
                .findByProductIdInOrderBySortOrderAsc(ids).stream()
                .collect(Collectors.groupingBy(i -> i.getProduct().getId()));
        Map<Long, List<ProductVariant>> variantsByProduct = variantRepository
                .findByProductIdIn(ids).stream()
                .collect(Collectors.groupingBy(v -> v.getProduct().getId()));

        List<Promotion> live = promotionService.livePromotions();

        return products.stream()
                .map(p -> {
                    Promotion promo = promotionService.matched(p, live);
                    short rate = promo == null ? p.getDiscountRate() : promo.getDiscountRate();
                    return ProductSummaryResponse.of(p,
                            imagesByProduct.getOrDefault(p.getId(), List.of()),
                            variantsByProduct.getOrDefault(p.getId(), List.of()),
                            rate, promo == null ? null : promo.getName());
                })
                .toList();
    }

    public ProductDetailResponse getDetail(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다: " + slug));

        Promotion promo = promotionService.matched(product, promotionService.livePromotions());
        short rate = promo == null ? product.getDiscountRate() : promo.getDiscountRate();

        return ProductDetailResponse.of(
                product,
                imageRepository.findByProductIdOrderBySortOrderAsc(product.getId()),
                variantRepository.findByProductId(product.getId()),
                measurementRepository.findByProductIdOrderBySizeIdAscIdAsc(product.getId()),
                rate, promo == null ? null : promo.getName());
    }
}