package com.arcmethod.catalog.dto;

import com.arcmethod.catalog.domain.Size;

public record SizeResponse(Long id, String name) {
    public static SizeResponse from(Size s){
        return new SizeResponse(s.getId(), s.getName());
    }
}
