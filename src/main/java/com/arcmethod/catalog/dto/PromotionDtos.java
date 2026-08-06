package com.arcmethod.catalog.dto;

import com.arcmethod.catalog.domain.Promotion;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

public class PromotionDtos {
    public record Response(
            Long id, String name, short discountRate, String scope, Long categoryId, List<Long> productIds, boolean isActive, OffsetDateTime startsAt, OffsetDateTime endsAt, int priority, boolean live){
        public static Response from(Promotion p){
            return new Response(p.getId(), p.getName(), p.getDiscountRate(), p.getScope(), p.getCategory() == null ? null : p.getCategory().getId(), List.copyOf(p.getProductIds()), p.isActive(), p.getStartsAt(), p.getEndsAt(), p.getPriority(), p.isLive());
        }
    }
    public record Request(
            @NotBlank String name,
            @Min(0) @Max(100) short discountRate,
            @NotBlank String scope,
            Long categoryId,
            Set<Long> productIds,
            Boolean isActive,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt,
            Integer priority){
        public boolean activeOrDefault(){return isActive == null || isActive;}
        public int priorityOrDefault(){return priority == null ? 0 : priority;}
    }
}
