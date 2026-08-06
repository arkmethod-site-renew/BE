package com.arcmethod.community.dto;

import com.arcmethod.community.domain.CommunityPost;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public class CommunityDtos {
    public record PostResponse(
            Long id, String boardType, String title, String content, String author, int viewCount, OffsetDateTime createdAt) {
        public static PostResponse from(CommunityPost p, String author) {
            return new PostResponse(p.getId(), p.getBoardType(), p.getTitle(), p.getContent(), author, p.getViewCount(), p.getCreatedAt());
        }

    }
    public record PostRequest(
        @NotBlank String boardType,
        @NotBlank String title,
        @NotBlank String content){

    }
}
