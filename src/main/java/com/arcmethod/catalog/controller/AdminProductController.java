package com.arcmethod.catalog.controller;
import com.arcmethod.catalog.dto.ProductAdminDtos.DiscountRequest;
import com.arcmethod.catalog.dto.ProductAdminDtos.FlagsRequest;
import com.arcmethod.catalog.dto.ProductAdminDtos.StatusRequest;
import com.arcmethod.catalog.dto.ProductAdminDtos.StockRequest;
import com.arcmethod.catalog.dto.ProductDetailResponse;
import com.arcmethod.catalog.dto.ProductSummaryResponse;
import com.arcmethod.catalog.service.ProductAdminService;
import com.arcmethod.catalog.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;
    private final ProductAdminService productAdminService;
    @GetMapping("/products")
    public List<ProductSummaryResponse> list(){
        return productService.getList(null, null);
    }
    @GetMapping("/products/{slug}")
    public ProductDetailResponse detail(@PathVariable String slug){
        return productService.getDetail(slug);
    }
    @PatchMapping("/products/{id}/discount")
    public ResponseEntity<Void> discount(@PathVariable Long id, @Valid @RequestBody DiscountRequest req){
        productAdminService.changeDiscount(id, req.discountRate());
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/products/{id}/status")
    public ResponseEntity<Void> status(@PathVariable Long id, @Valid @RequestBody StatusRequest req){
        productAdminService.changeStatus(id, req.status());
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/products/{id}/flags")
    public ResponseEntity<Void> flags(@PathVariable Long id, @RequestBody FlagsRequest req){
        productAdminService.changeFlags(id, req.isNew(), req.isBest());
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/variants/{variantId}/stock")
    public ResponseEntity<Void> stock(@PathVariable Long variantId, @Valid @RequestBody StockRequest req){
        productAdminService.changeStock(variantId, req.stockQty());
        return ResponseEntity.noContent().build();
    }
}
