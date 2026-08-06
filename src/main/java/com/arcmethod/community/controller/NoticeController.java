package com.arcmethod.community.controller;

import com.arcmethod.community.dto.NoticeDtos.Response;
import com.arcmethod.community.service.NoticeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public List<Response> live() {
        return noticeService.findLive();
    }
}