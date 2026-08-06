package com.arcmethod.review.dto;

import com.arcmethod.review.domain.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.OffsetDateTime;
import java.util.List;

public class ReviewDtos {
    public record Response(
            Long id, String author, short rating, String content, Short reviewerHeight, Short reviewerWeight, String sizePurchased, OffsetDateTime createdAt){
        public static Response from(Review r, String author){
            return new Response(r.getId(), author, r.getRating(), r.getContent(), r.getReviewerHeight(), r.getReviewerWeight(), r.getSizePurchased(), r.getCreatedAt());
        }
    }
    public record Summary(double average, int count){}
    public record ListResponse(Summary summary, List<Response> items){}
    public record Request(@Min(1) @Max(5) short rating, String content, Short reviewerHeight, Short reviewerWeight, String sizePurchased){}
}
