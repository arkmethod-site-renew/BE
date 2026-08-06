package com.arcmethod.order.controller;

import com.arcmethod.order.dto.OrderAdminDtos.Response;
import com.arcmethod.order.dto.OrderAdminDtos.StatusRequest;
import com.arcmethod.order.service.OrderAdminService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderAdminService orderAdminService;
    @GetMapping
    public List<Response>list(){
        return orderAdminService.findAll();
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> status(@PathVariable Long id, @Valid @RequestBody StatusRequest req){
        orderAdminService.changeStatus(id, req.status());
        return ResponseEntity.noContent().build();
    }
}
