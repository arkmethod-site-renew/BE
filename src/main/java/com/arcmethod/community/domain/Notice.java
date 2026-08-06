package com.arcmethod.community.domain;

import com.arcmethod.common.entity.BaseTimeEntity;
import com.arcmethod.common.entity.Schedulable;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notice")
public class Notice extends BaseTimeEntity implements Schedulable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    @Column(nullable = false, length = 30)
    private String category;         // GENERAL / SHIPPING / RESTOCK / EVENT
    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    @Column(name = "starts_at")
    private OffsetDateTime startsAt;
    @Column(name = "ends_at")
    private OffsetDateTime endsAt;
    @Column(name = "view_count", nullable = false)
    private int viewCount;
    @Column(name = "author_id")
    private Long authorId;
    @Override
    public boolean isActive() {
        return active;
    }
    public void increaseView() {
        this.viewCount++;
    }
    public void update(String title, String content, String category,
                       boolean pinned, boolean active,
                       OffsetDateTime startsAt, OffsetDateTime endsAt) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.pinned = pinned;
        this.active = active;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }
    public static Notice create(String title, String content, String category,
                                boolean pinned, boolean active,
                                OffsetDateTime startsAt, OffsetDateTime endsAt, Long authorId) {
        Notice n = new Notice();
        n.update(title, content, category, pinned, active, startsAt, endsAt);
        n.authorId = authorId;
        return n;
    }
}