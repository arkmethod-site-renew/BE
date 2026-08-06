package com.arcmethod.catalog.controller;
import com.arcmethod.catalog.dto.PromotionDtos.Request;
import com.arcmethod.catalog.dto.PromotionDtos.Response;
import com.arcmethod.catalog.service.PromotionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/promotions")
@RequiredArgsConstructor
public class AdminPromotionController {
    private final PromotionService promotionService;
    @GetMapping
    public List<Response> list(){
        return promotionService.findAll();
    }
    @GetMapping("/{id}")
    public Response detail(@PathVariable Long id){
        return promotionService.findOne(id);
    }
    @PostMapping
    public Response create(@Valid @RequestBody Request req){
        return promotionService.create(req);
    }
    @PutMapping("/{id}")
    public Response update(@PathVariable Long id, @Valid @RequestBody Request req){
        return promotionService.update(id, req);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        promotionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
