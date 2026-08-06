package com.arcmethod.catalog.repository;

import com.arcmethod.catalog.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);
    List<Product> findByIsNewTrueOrderByCreatedAtDesc();
    List<Product> findByIsBestTrueOrderByCreatedAtDesc();
    List<Product> findByCategory_SlugOrderByCreatedAtDesc(String slug);
}