package com.arcmethod.common.entity;

import java.time.OffsetDateTime;

public interface Schedulable {
    boolean isActive();
    OffsetDateTime getStartsAt();
    OffsetDateTime getEndsAt();
    default boolean isLive(){
        if(!isActive()) return false;
        OffsetDateTime now = OffsetDateTime.now();
        if(getStartsAt() != null && now.isBefore(getStartsAt())) return false;
        if(getEndsAt() != null && !now.isBefore(getEndsAt()))return false;
        return true;
    }
}