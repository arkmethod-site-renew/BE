package com.arcmethod.member.controller;

import com.arcmethod.member.domain.Wishlist;
import com.arcmethod.member.repository.MemberRepository;
import com.arcmethod.member.repository.WishlistRepository;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<List<Long>> list(Principal principal) {
        Long id = memberId(principal);
        if (id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(
                wishlistRepository.findByMemberIdOrderByCreatedAtDesc(id)
                        .stream().map(Wishlist::getProductId).toList());
    }

    @PostMapping("/{productId}")
    @Transactional
    public ResponseEntity<Void> add(@PathVariable Long productId, Principal principal) {
        Long id = memberId(principal);
        if (id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        wishlistRepository.findByMemberIdAndProductId(id, productId)
                .orElseGet(() -> wishlistRepository.save(Wishlist.of(id, productId)));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    @Transactional
    public ResponseEntity<Void> remove(@PathVariable Long productId, Principal principal) {
        Long id = memberId(principal);
        if (id == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        wishlistRepository.deleteByMemberIdAndProductId(id, productId);
        return ResponseEntity.noContent().build();
    }

    private Long memberId(Principal p) {
        if (p == null) return null;
        return memberRepository.findByEmail(p.getName()).map(m -> m.getId()).orElse(null);
    }
}