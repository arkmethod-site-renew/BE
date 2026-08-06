package com.arcmethod.common.service;

import com.arcmethod.catalog.domain.Banner;
import com.arcmethod.catalog.domain.Product;
import com.arcmethod.catalog.domain.ProductStatus;
import com.arcmethod.catalog.repository.BannerRepository;
import com.arcmethod.catalog.repository.ProductRepository;
import com.arcmethod.catalog.service.PromotionService;
import com.arcmethod.common.dto.DashboardResponse;
import com.arcmethod.member.repository.MemberRepository;
import com.arcmethod.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final BannerRepository bannerRepository;
    private final PromotionService promotionService;
    public DashboardResponse stats(){
        long soldout = productRepository.findAll().stream().map(Product::getStatus).filter(s -> s == ProductStatus.SOLDOUT).count();
        long liveBanners = bannerRepository.findAll().stream().filter(Banner::isLive).count();
        return new DashboardResponse(
                productRepository.count(), soldout, memberRepository.count(), orderRepository.count(), orderRepository.sumRevenue(), liveBanners, promotionService.livePromotions().size());
    }
}
