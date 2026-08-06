package com.arcmethod.catalog.dto;

import com.arcmethod.catalog.domain.Color;

public record ColorResponse(Long id, String name, String hex) {
    public static ColorResponse from(Color c){
        return new ColorResponse(c.getId(), c.getName(), c.getHex());
    }
}
