package com.arcmethod.catalog.dto;

import com.arcmethod.catalog.domain.ProductMeasurement;

import java.math.BigDecimal;

public record MeasurementResponse(Long sizeId, String itemKey, BigDecimal valueCm) {
    public static MeasurementResponse from(ProductMeasurement m){
        return new MeasurementResponse(m.getSize().getId(), m.getItemKey(), m.getValueCm());
    }
}
