package com.arcmethod.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class CheckoutDtos {

    public record Line(Long variantId, @Min(1) int quantity) {}

    // 컬렉션에는 @NotBlank(String 전용)가 아니라 @NotEmpty를 쓴다.
    public record Request(
            @NotEmpty List<Line> lines,
            @NotBlank String receiverName,
            @NotBlank String receiverPhone,
            @NotBlank String address) {}

    public record Response(Long orderId, String orderNo, int totalAmount, String status) {}
}
