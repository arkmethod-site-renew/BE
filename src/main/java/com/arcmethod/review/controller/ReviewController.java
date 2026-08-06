package com.arcmethod.review.controller;
import com.arcmethod.member.repository.MemberRepository;
import com.arcmethod.review.dto.ReviewDtos.ListResponse;
import com.arcmethod.review.dto.ReviewDtos.Request;
import com.arcmethod.review.dto.ReviewDtos.Response;
import com.arcmethod.review.service.ReviewService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{slug}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;
    @GetMapping
    public ListResponse list(@PathVariable String slug){
        return reviewService.findByProductSlug(slug);
    }
    @PostMapping
    public ResponseEntity<Response> create(@PathVariable String slug, @Valid @RequestBody Request req, Principal principal){
        if(principal == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long memberId = memberRepository.findByEmail(principal.getName()).orElseThrow().getId();
        return ResponseEntity.ok(reviewService.create(slug, memberId, req));
    }
}
