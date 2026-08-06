package com.arcmethod.member.dto;

import com.arcmethod.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public class MemberAdminDtos {

    public record Response(
            Long id, String email, String name, String phone, String role, String status, OffsetDateTime lastLoginAt, String memo, OffsetDateTime createdAt) {
        public static Response from(Member m) {
            return new Response(m.getId(), m.getEmail(), m.getName(), m.getPhone(), m.getRole(), m.getStatus(), m.getLastLoginAt(), m.getMemo(), m.getCreatedAt());
        }
    }
    public record StatusRequest(@NotBlank String status){}
    public record MemoRequest(String memo){}
}