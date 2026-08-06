package com.arcmethod.catalog.dto;

import com.arcmethod.catalog.domain.ProductVariant;

public record ProductVariantResponse(Long id, Long colorId, Long sizeId, String sku, int stockQty, int additionalPrice) {
    public static ProductVariantResponse from(ProductVariant v){
        return new ProductVariantResponse(v.getId(), v.getColor().getId(), v.getSize().getId(), v.getSku(), v.getStockQty(), v.getAdditionalPrice());
    }
}
