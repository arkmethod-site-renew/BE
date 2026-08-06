package com.arcmethod.catalog.dto;
import com.arcmethod.catalog.domain.Season;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public class SeasonDtos {
    public record Response(Long id, String code, String name, String concept, boolean isActive, OffsetDateTime startsAt, OffsetDateTime endsAt, int sortOrder, boolean live){
        public static Response from(Season s){
            return new Response(s.getId(), s.getCode(), s.getName(), s.getConcept(), s.isActive(), s.getStartsAt(), s.getEndsAt(), s.getSortOrder(), s.isLive());
        }
    }
    public record Request(
            @NotBlank String code,
            @NotBlank String name,
            String concept,
            Boolean isActive,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt,
            Integer sortOrder){
        public boolean activeOrDefault(){
            return isActive == null || isActive;
        }
        public int sortOrDefault(){
            return sortOrder == null ? 0 : sortOrder;
        }
    }
}
