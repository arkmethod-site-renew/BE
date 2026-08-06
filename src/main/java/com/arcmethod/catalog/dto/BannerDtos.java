package com.arcmethod.catalog.dto;
import com.arcmethod.catalog.domain.Banner;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public class BannerDtos {
    public record Response(
            Long id, String position, String title, String subtitle, String imageUrl, String mobileImageUrl, String linkUrl, String textColor,
            boolean isActive, OffsetDateTime startsAt, OffsetDateTime endsAt, int sortOrder, boolean live){
        public static Response from(Banner b) {
            return new Response(b.getId(), b.getPosition(), b.getTitle(), b.getSubtitle(),
                    b.getImageUrl(), b.getMobileImageUrl(), b.getLinkUrl(), b.getTextColor(),
                    b.isActive(), b.getStartsAt(), b.getEndsAt(), b.getSortOrder(), b.isLive());
        }
    }
    public record Request(
            @NotBlank String position,
            @NotBlank String title,
            String subtitle,
            String imageUrl,
            String mobileImageUrl,
            String linkUrl,
            String textColor,
            Boolean isActive,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt,
            Integer sortOrder
    ){
        public boolean activeOrDefault(){return isActive == null || isActive;}
        public int sortOrDefault(){return sortOrder == null ? 0 : sortOrder;}
        public String imageOrEmpty(){return imageUrl == null ? "" : imageUrl;}
        public String colorOrDefault(){return textColor == null ? "LIGHT" : textColor;}
    }
}
