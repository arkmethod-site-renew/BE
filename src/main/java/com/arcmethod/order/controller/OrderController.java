package com.arcmethod.order.controller;

import com.arcmethod.member.repository.MemberRepository;
import com.arcmethod.order.dto.CheckoutDtos.Request;
import com.arcmethod.order.dto.CheckoutDtos.Response;
import com.arcmethod.order.dto.OrderAdminDtos;
import com.arcmethod.order.service.CheckoutService;
import com.arcmethod.order.service.OrderAdminService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CheckoutService checkoutService;
    private final OrderAdminService orderAdminService;
    private final MemberRepository memberRepository;

    /** 내 주문 내역 (Mypage) — 매핑은 서비스(트랜잭션) 안에서 한다 */
    @GetMapping
    public ResponseEntity<List<OrderAdminDtos.Response>> myOrders(Principal principal) {
        Long id = memberId(principal);
        if (id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(orderAdminService.findByMember(id));
    }

    @PostMapping("/checkout")
    public ResponseEntity<Response> checkout(@Valid @RequestBody Request req,
                                             Principal principal) {
        Long id = memberId(principal);
        if (id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(checkoutService.checkout(id, req));
    }

    private Long memberId(Principal p) {
        if (p == null) return null;
        return memberRepository.findByEmail(p.getName()).map(m -> m.getId()).orElse(null);
    }
}