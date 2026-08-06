package com.arcmethod.catalog.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProductAdminDtos {
    public record DiscountRequest(@Min(0) @Max(100) short discountRate){}
    public record StatusRequest(@NotBlank String status){}
    public record StockRequest(@Min(0) int stockQty){}
    public record FlagsRequest(boolean isNew, boolean isBest){}
}
