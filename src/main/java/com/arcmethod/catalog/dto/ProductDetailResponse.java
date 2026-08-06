package com.arcmethod.catalog.dto;

import com.arcmethod.catalog.domain.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public record ProductDetailResponse(
        Long id, String name, String slug, String description,
        CategoryResponse category,
        int price, short discountRate, int salePrice,
        String status, boolean isNew, boolean isBest, LocalDate preorderShipDate,
        String material, String careInstructions, String thickness, String elasticity,
        String transparency, String lining, String season,
        Short modelHeightCm, Short modelWeightKg, String modelSizeWorn,
        List<ProductImageResponse> images,
        List<ProductVariantResponse> variants,
        // record 선언 마지막 줄을 이렇게:
        List<MeasurementResponse> measurements,
        String thumbnail,
        List<ColorResponse> colors,
        List<SizeResponse> sizes,
        String promotionName) {

    /** 상품 자체 할인율 */
    public static ProductDetailResponse of(
            Product p, List<ProductImage> images,
            List<ProductVariant> variants, List<ProductMeasurement> measurements) {
        return of(p, images, variants, measurements, p.getDiscountRate(), null);
    }

    /** 프로모션 적용 할인율 */
    public static ProductDetailResponse of(
            Product p, List<ProductImage> images,
            List<ProductVariant> variants, List<ProductMeasurement> measurements,
            short effectiveRate, String promotionName) {

        String thumb = images.stream()
                .filter(i -> i.getImageType() == ImageType.MAIN).findFirst()
                .map(ProductImage::getUrl)
                .orElse(images.isEmpty() ? null : images.get(0).getUrl());

        List<ColorResponse> colors = variants.stream()
                .map(ProductVariant::getColor)
                .collect(Collectors.toMap(Color::getId, c -> c, (a, b) -> a, LinkedHashMap::new))
                .values().stream().map(ColorResponse::from).toList();

        List<SizeResponse> sizes = variants.stream()
                .map(ProductVariant::getSize)
                .collect(Collectors.toMap(Size::getId, s -> s, (a, b) -> a, LinkedHashMap::new))
                .values().stream().map(SizeResponse::from).toList();

        int sale = Math.round(p.getPrice() * (100 - effectiveRate) / 100.0f);

        return new ProductDetailResponse(
                p.getId(), p.getName(), p.getSlug(), p.getDescription(),
                CategoryResponse.from(p.getCategory()),
                p.getPrice(), effectiveRate, sale,
                p.getStatus().name(), p.isNew(), p.isBest(), p.getPreorderShipDate(),
                p.getMaterial(), p.getCareInstructions(), p.getThickness(), p.getElasticity(),
                p.getTransparency(), p.getLining(), p.getSeason(),
                p.getModelHeightCm(), p.getModelWeightKg(), p.getModelSizeWorn(),
                images.stream().map(ProductImageResponse::from).toList(),
                variants.stream().map(ProductVariantResponse::from).toList(),
                measurements.stream().map(MeasurementResponse::from).toList(),
                thumb, colors, sizes, promotionName);
    }
}