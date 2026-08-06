package com.arcmethod.community.dto;

import com.arcmethod.community.domain.Notice;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public class NoticeDtos {
    public record Response(
            Long id, String title, String content, String category, boolean isPinned, boolean isActive, OffsetDateTime startsAt, OffsetDateTime endsAt, int viewCount, OffsetDateTime createdAt, boolean live){
        public static Response from(Notice n){
            return new Response(n.getId(), n.getTitle(), n.getContent(), n.getCategory(), n.isPinned(), n.isActive(), n.getStartsAt(), n.getEndsAt(), n.getViewCount(), n.getCreatedAt(), n.isLive());
        }
    }
    public record Request(
            @NotBlank String title,
            @NotBlank String content,
            String category, Boolean isPinned, Boolean isActive, OffsetDateTime startsAt, OffsetDateTime endsAt){
        public String categoryOrDefault(){return category == null ? "GENERAL": category;}
        public boolean pinnedOrDefault(){return isPinned != null && isPinned;}
        public boolean activeOrDefault(){return isActive == null || isActive;}
    }

}
