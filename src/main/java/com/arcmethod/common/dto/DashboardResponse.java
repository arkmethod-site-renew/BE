package com.arcmethod.common.dto;

public record DashboardResponse(
        long productCount,
        long soldoutCount,
        long memberCount,
        long orderCount,
        long revenue,
        long activeBanners,
        long livePromotions
) {
}
