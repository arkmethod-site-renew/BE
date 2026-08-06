package com.arcmethod.catalog.dto;

import com.arcmethod.catalog.domain.Color;
import com.arcmethod.catalog.domain.ImageType;
import com.arcmethod.catalog.domain.Product;
import com.arcmethod.catalog.domain.ProductImage;
import com.arcmethod.catalog.domain.ProductVariant;
import com.arcmethod.catalog.domain.Size;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public record ProductSummaryResponse(
        Long id, String name, String slug,
        int price, short discountRate, int salePrice,
        String status, boolean isNew, boolean isBest,
        String thumbnail, String hoverImage,
        List<ColorResponse> colors,
        List<SizeResponse> sizes,
        String categorySlug,
        String promotionName) {

    /** 상품 자체 할인율 사용 */
    public static ProductSummaryResponse of(
            Product p, List<ProductImage> images, List<ProductVariant> variants) {
        return of(p, images, variants, p.getDiscountRate(), null);
    }

    /** 프로모션이 적용된 할인율 사용 */
    public static ProductSummaryResponse of(
            Product p, List<ProductImage> images, List<ProductVariant> variants,
            short effectiveRate, String promotionName) {

        String thumb = images.stream()
                .filter(i -> i.getImageType() == ImageType.MAIN).findFirst()
                .map(ProductImage::getUrl)
                .orElse(images.isEmpty() ? null : images.get(0).getUrl());

        String hover = images.stream()
                .filter(i -> i.getImageType() == ImageType.HOVER).findFirst()
                .map(ProductImage::getUrl).orElse(null);

        List<ColorResponse> colors = variants.stream()
                .map(ProductVariant::getColor)
                .collect(Collectors.toMap(Color::getId, c -> c, (a, b) -> a, LinkedHashMap::new))
                .values().stream().map(ColorResponse::from).toList();

        List<SizeResponse> sizes = variants.stream()
                .map(ProductVariant::getSize)
                .collect(Collectors.toMap(Size::getId, s -> s, (a, b) -> a, LinkedHashMap::new))
                .values().stream().map(SizeResponse::from).toList();

        int sale = Math.round(p.getPrice() * (100 - effectiveRate) / 100.0f);

        return new ProductSummaryResponse(
                p.getId(), p.getName(), p.getSlug(),
                p.getPrice(), effectiveRate, sale,
                p.getStatus().name(), p.isNew(), p.isBest(),
                thumb, hover, colors, sizes,
                p.getCategory() == null ? null : p.getCategory().getSlug(),
                promotionName);
    }
}