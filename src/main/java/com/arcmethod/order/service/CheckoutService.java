package com.arcmethod.order.service;

import com.arcmethod.catalog.domain.ProductVariant;
import com.arcmethod.catalog.repository.ProductVariantRepository;
import com.arcmethod.order.domain.Order;
import com.arcmethod.order.domain.OrderItem;
import com.arcmethod.order.dto.CheckoutDtos.Request;
import com.arcmethod.order.dto.CheckoutDtos.Response;
import com.arcmethod.order.repository.OrderItemRepository;
import com.arcmethod.order.repository.OrderRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductVariantRepository variantRepository;

    /** 개념 데모: 실제 결제 없음. 재고만 차감하고 PENDING 주문을 만든다. */
    public Response checkout(Long memberId, Request req) {
        Order order = orderRepository.save(Order.create(
                memberId, generateOrderNo(),
                req.receiverName(), req.receiverPhone(), req.address()));

        int total = 0;
        for (var line : req.lines()) {
            ProductVariant v = variantRepository.findById(line.variantId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "옵션을 찾을 수 없습니다: " + line.variantId()));

            if (v.getStockQty() < line.quantity()) {
                throw new IllegalStateException(
                        "재고가 부족합니다: " + v.getProduct().getName());
            }
            v.changeStock(v.getStockQty() - line.quantity());

            int unit = v.getProduct().salePrice() + v.getAdditionalPrice();
            total += unit * line.quantity();

            orderItemRepository.save(OrderItem.create(order, v.getId(),
                    v.getProduct().getName(), v.getColor().getName(), v.getSize().getName(),
                    unit, line.quantity()));
        }

        order.applyTotal(total);
        return new Response(order.getId(), order.getOrderNo(), total, order.getStatus());
    }

    private String generateOrderNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int rnd = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "ARC-" + date + "-" + rnd;
    }
}