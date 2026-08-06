package com.arcmethod.community.repository;

import com.arcmethod.community.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByOrderByPinnedDescCreatedAtDesc();
}
