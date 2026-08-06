package com.arcmethod.catalog.repository;

import com.arcmethod.catalog.domain.Banner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByOrderByPositionAscSortOrderAscIdAsc();
    List<Banner> findByPositionOrderBySortOrderAscIdAsc(String position);
}