package com.arcmethod.catalog.repository;

import com.arcmethod.catalog.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findAllByOrderByPriorityDescIdDesc();
}
