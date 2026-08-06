package com.arcmethod.catalog.domain;

import com.arcmethod.common.entity.BaseTimeEntity;
import com.arcmethod.common.entity.Schedulable;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "banner")
public class Banner extends BaseTimeEntity implements Schedulable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String position;          // MAIN_HERO / MAIN_STRIP / SHOP_TOP / POPUP

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 300)
    private String subtitle;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "mobile_image_url", length = 500)
    private String mobileImageUrl;

    @Column(name = "link_url", length = 500)
    private String linkUrl;

    @Column(name = "text_color", nullable = false, length = 20)
    private String textColor = "LIGHT";

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "starts_at")
    private OffsetDateTime startsAt;

    @Column(name = "ends_at")
    private OffsetDateTime endsAt;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Override
    public boolean isActive() {
        return active;
    }

    public void update(String position, String title, String subtitle,
                       String imageUrl, String mobileImageUrl, String linkUrl, String textColor,
                       boolean active, OffsetDateTime startsAt, OffsetDateTime endsAt, int sortOrder) {
        this.position = position;
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.mobileImageUrl = mobileImageUrl;
        this.linkUrl = linkUrl;
        this.textColor = textColor;
        this.active = active;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.sortOrder = sortOrder;
    }

    public static Banner create(String position, String title, String subtitle,
                                String imageUrl, String mobileImageUrl, String linkUrl, String textColor,
                                boolean active, OffsetDateTime startsAt, OffsetDateTime endsAt, int sortOrder) {
        Banner b = new Banner();
        b.update(position, title, subtitle, imageUrl, mobileImageUrl, linkUrl, textColor,
                active, startsAt, endsAt, sortOrder);
        return b;
    }
}