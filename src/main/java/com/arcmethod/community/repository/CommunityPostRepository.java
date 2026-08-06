package com.arcmethod.community.repository;

import com.arcmethod.community.domain.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    List<CommunityPost> findByBoardTypeOrderByCreatedAtDesc(String boardType);
    List<CommunityPost> findAllByOrderByCreatedAtDesc();
}
