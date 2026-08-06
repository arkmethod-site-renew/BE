package com.arcmethod.catalog.repository;

import com.arcmethod.catalog.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Category, Long> {
}
