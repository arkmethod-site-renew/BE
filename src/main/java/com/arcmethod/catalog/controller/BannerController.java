package com.arcmethod.catalog.controller;
import com.arcmethod.catalog.dto.BannerDtos.Response;
import com.arcmethod.catalog.service.BannerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;
    //GET
    @GetMapping
    public List<Response> live(@RequestParam(required = false) String position){
        return bannerService.findLive(position);
    }
}
