package com.arcmethod.catalog.repository;

import com.arcmethod.catalog.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findAllByOrderBySortOrderAscIdAsc();
}
