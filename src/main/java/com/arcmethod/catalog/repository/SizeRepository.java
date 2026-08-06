package com.arcmethod.catalog.repository;

import com.arcmethod.catalog.domain.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {
}