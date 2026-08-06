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
@Table(name = "season")
public class Season extends BaseTimeEntity implements Schedulable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20, unique = true)
    private String code;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(length = 300)
    private String concept;
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    @Column(name = "starts_at")
    private OffsetDateTime startsAt;
    @Column(name = "ends_at")
    private OffsetDateTime endsAt;
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
    @Override
    public boolean isActive(){
        return active;
    }
    public void update(String code, String name, String concept, boolean active, OffsetDateTime startsAt, OffsetDateTime endsAt, int sortOrder){
        this.code = code;
        this.name = name;
        this.concept = concept;
        this.active = active;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.sortOrder = sortOrder;
    }
    public static Season create(String code, String name, String concept, boolean active, OffsetDateTime startsAt, OffsetDateTime endsAt, int sortOrder){
        Season s = new Season();
        s.update(code, name, concept, active, startsAt, endsAt, sortOrder);
        return s;
    }
}
