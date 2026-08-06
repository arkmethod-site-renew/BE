package com.arcmethod.order.dto;

import com.arcmethod.order.domain.Order;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
public class OrderAdminDtos {
    public record Response(
            Long id, String orderNo, String status, int totalAmount, String receiverName, OffsetDateTime createdAt, int itemCount){
        public static Response from(Order o){
            return new Response(o.getId(), o.getOrderNo(), o.getStatus(), o.getTotalAmount(), o.getReceiverName(), o.getCreatedAt(), o.getItems().size());
        }
    }
    public record StatusRequest(@NotBlank String status){
    }
}
