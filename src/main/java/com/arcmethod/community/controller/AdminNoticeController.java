package com.arcmethod.community.controller;
import com.arcmethod.community.dto.NoticeDtos.Request;
import com.arcmethod.community.dto.NoticeDtos.Response;
import com.arcmethod.community.service.NoticeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {
    private final NoticeService noticeService;
    @GetMapping
    public List<Response> list(){
        return noticeService.findAll();
    }
    @GetMapping("/{id}")
    public Response detail(@PathVariable Long id){
        return noticeService.findOne(id);
    }
    @PostMapping
    public Response create(@Valid @RequestBody Request req){
        return noticeService.create(req);
    }
    @PutMapping("/{id}")
    public Response update(@PathVariable Long id, @Valid @RequestBody Request req){
        return noticeService.update(id, req);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        noticeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
