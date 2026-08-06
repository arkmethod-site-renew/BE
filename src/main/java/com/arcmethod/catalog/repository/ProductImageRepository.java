package com.arcmethod.catalog.repository;

import com.arcmethod.catalog.domain.ProductImage;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);
    List<ProductImage> findByProductIdInOrderBySortOrderAsc(Collection<Long> productIds);
}