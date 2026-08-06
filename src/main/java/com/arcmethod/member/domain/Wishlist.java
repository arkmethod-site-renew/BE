package com.arcmethod.member.domain;
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
@Table(name = "wishlist")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;
    public static Wishlist of(Long memberId, Long productId){
        Wishlist w = new Wishlist();
        w.memberId = memberId;
        w.productId = productId;
        return w;
    }
}
