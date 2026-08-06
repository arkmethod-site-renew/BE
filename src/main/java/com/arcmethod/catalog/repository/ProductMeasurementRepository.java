package com.arcmethod.catalog.repository;

import com.arcmethod.catalog.domain.ProductMeasurement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMeasurementRepository extends JpaRepository<ProductMeasurement, Long> {
    List<ProductMeasurement> findByProductIdOrderBySizeIdAscIdAsc(Long productId);
}