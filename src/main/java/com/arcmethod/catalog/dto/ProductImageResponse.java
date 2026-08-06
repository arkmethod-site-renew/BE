package com.arcmethod.catalog.dto;

import com.arcmethod.catalog.domain.ProductImage;

public record ProductImageResponse(
        Long id, String url, String alt, String imageType, Long colorId, int sortOrder) {

    public static ProductImageResponse from(ProductImage i) {
        return new ProductImageResponse(
                i.getId(), i.getUrl(), i.getAlt(), i.getImageType().name(),
                i.getColor() == null ? null : i.getColor().getId(), i.getSortOrder());
    }
}