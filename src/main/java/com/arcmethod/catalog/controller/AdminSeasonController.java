package com.arcmethod.catalog.controller;

import com.arcmethod.catalog.dto.SeasonDtos.Request;
import com.arcmethod.catalog.dto.SeasonDtos.Response;
import com.arcmethod.catalog.service.SeasonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/seasons")
@RequiredArgsConstructor
public class AdminSeasonController {

    private final SeasonService seasonService;

    @GetMapping
    public List<Response> list() {
        return seasonService.findAll();
    }

    @GetMapping("/{id}")
    public Response detail(@PathVariable Long id) {
        return seasonService.findOne(id);
    }

    @PostMapping
    public Response create(@Valid @RequestBody Request req) {
        return seasonService.create(req);
    }

    @PutMapping("/{id}")
    public Response update(@PathVariable Long id, @Valid @RequestBody Request req) {
        return seasonService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seasonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}