package com.arcmethod.catalog.domain;

import com.arcmethod.common.entity.BaseTimeEntity;
import com.arcmethod.common.entity.Schedulable;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "promotion")
public class Promotion extends BaseTimeEntity implements Schedulable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200)
    private String name;
    @Column(name = "discount_rate", nullable = false)
    private short discountRate;
    @Column(nullable = false, length = 20)
    private String scope;            // ALL / CATEGORY / PRODUCT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    @Column(name = "starts_at")
    private OffsetDateTime startsAt;
    @Column(name = "ends_at")
    private OffsetDateTime endsAt;
    @Column(nullable = false)
    private int priority;
    @ElementCollection
    @CollectionTable(name = "promotion_product", joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "product_id")
    private Set<Long> productIds = new HashSet<>();
    @Override
    public boolean isActive() {
        return active;
    }
    public boolean appliesTo(Product p) {
        if (!isLive()) return false;
        return switch (scope) {
            case "ALL" -> true;
            case "CATEGORY" -> category != null
                    && p.getCategory() != null
                    && category.getId().equals(p.getCategory().getId());
            case "PRODUCT" -> productIds.contains(p.getId());
            default -> false;
        };
    }
    public void update(String name, short discountRate, String scope, Category category,
                       boolean active, OffsetDateTime startsAt, OffsetDateTime endsAt,
                       int priority, Set<Long> productIds) {
        this.name = name;
        this.discountRate = discountRate;
        this.scope = scope;
        this.category = category;
        this.active = active;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.priority = priority;
        this.productIds = productIds == null ? new HashSet<>() : new HashSet<>(productIds);
    }
    public static Promotion create(String name, short discountRate, String scope, Category category,
                                   boolean active, OffsetDateTime startsAt, OffsetDateTime endsAt,
                                   int priority, Set<Long> productIds) {
        Promotion p = new Promotion();
        p.update(name, discountRate, scope, category, active, startsAt, endsAt, priority, productIds);
        return p;
    }
}