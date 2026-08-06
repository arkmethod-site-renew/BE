package com.arcmethod.member.dto;

import com.arcmethod.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password){}
    public record MeResponse(Long id, String email, String name, String role){
        public static MeResponse from(Member m){
            return new MeResponse(m.getId(), m.getEmail(), m.getName(), m.getRole());
        }
    }
}
