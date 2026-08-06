package com.arcmethod.catalog.controller;
import com.arcmethod.catalog.dto.BannerDtos.Request;
import com.arcmethod.catalog.dto.BannerDtos.Response;
import com.arcmethod.catalog.service.BannerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {
    private final BannerService bannerService;
    @GetMapping
    public List<Response>list(){
        return bannerService.findAll();
    }
    @GetMapping("/{id}")
    public Response detail(@PathVariable Long id){
        return bannerService.findOne(id);
    }
    @PostMapping
    public Response create(@Valid @RequestBody Request req){
        return bannerService.create(req);
    }
    @PutMapping("/{id}")
    public Response update(@PathVariable Long id, @Valid @RequestBody Request req) {
        return bannerService.update(id, req);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        bannerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
