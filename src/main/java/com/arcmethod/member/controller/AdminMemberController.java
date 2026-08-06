package com.arcmethod.member.controller;

import com.arcmethod.member.dto.MemberAdminDtos.MemoRequest;
import com.arcmethod.member.dto.MemberAdminDtos.Response;
import com.arcmethod.member.dto.MemberAdminDtos.StatusRequest;
import com.arcmethod.member.service.MemberAdminService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberAdminService memberAdminService;

    @GetMapping
    public List<Response> list() {
        return memberAdminService.findAll();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> status(@PathVariable Long id,
                                       @Valid @RequestBody StatusRequest req) {
        memberAdminService.changeStatus(id, req.status());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/memo")
    public ResponseEntity<Void> memo(@PathVariable Long id,
                                     @RequestBody MemoRequest req) {
        memberAdminService.changeMemo(id, req.memo());
        return ResponseEntity.noContent().build();
    }
}