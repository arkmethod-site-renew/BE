package com.arcmethod.review.domain;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    @Column(nullable = false)
    private short rating;
    @Column(columnDefinition = "text")
    private String content;
    @Column(name = "reviewer_height")
    private Short reviewerHeight;
    @Column(name = "reviewer_weight")
    private Short reviewerWeight;
    @Column(name = "size_purchased", length = 20)
    private String sizePurchased;
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;
    public static Review create(Long productId, Long memberId, short rating, String content, Short height, Short weight, String sizePurchased){
        Review r = new Review();
        r.productId = productId;
        r.memberId = memberId;
        r.rating = rating;
        r.content = content;
        r.reviewerHeight = height;
        r.reviewerWeight = weight;
        r.sizePurchased = sizePurchased;
        return r;
    }
}
