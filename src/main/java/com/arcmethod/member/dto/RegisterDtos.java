package com.arcmethod.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDtos {
    public record Request(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 8, max = 64) String password,
            @NotBlank String name, String phone){
    }
}
