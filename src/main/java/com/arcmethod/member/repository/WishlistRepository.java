package com.arcmethod.member.repository;

import com.arcmethod.member.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    Optional<Wishlist> findByMemberIdAndProductId(Long memberId, Long productId);
    void deleteByMemberIdAndProductId(Long memberId, Long productId);
}
