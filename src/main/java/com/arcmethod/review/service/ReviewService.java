package com.arcmethod.review.service;

import com.arcmethod.catalog.repository.ProductRepository;
import com.arcmethod.member.repository.MemberRepository;
import com.arcmethod.review.domain.Review;
import com.arcmethod.review.dto.ReviewDtos.ListResponse;
import com.arcmethod.review.dto.ReviewDtos.Request;
import com.arcmethod.review.dto.ReviewDtos.Response;
import com.arcmethod.review.dto.ReviewDtos.Summary;
import com.arcmethod.review.repository.ReviewRepository;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ListResponse findByProductSlug(String slug) {
        Long productId = productRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다: " + slug))
                .getId();

        List<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
        if (reviews.isEmpty()) {
            return new ListResponse(new Summary(0, 0), List.of());
        }

        Map<Long, String> names = memberRepository.findAllById(
                        reviews.stream().map(Review::getMemberId).distinct().toList())
                .stream().collect(Collectors.toMap(m -> m.getId(), m -> mask(m.getName()),
                        (a, b) -> a));

        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0);

        List<Response> items = reviews.stream()
                .map(r -> Response.from(r, names.getOrDefault(r.getMemberId(), "익명")))
                .toList();

        return new ListResponse(new Summary(Math.round(avg * 10) / 10.0, reviews.size()), items);
    }

    @Transactional
    public Response create(String slug, Long memberId, Request req) {
        Long productId = productRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다: " + slug))
                .getId();

        Review r = reviewRepository.save(Review.create(productId, memberId, req.rating(),
                req.content(), req.reviewerHeight(), req.reviewerWeight(), req.sizePurchased()));

        String author = memberRepository.findById(memberId)
                .map(m -> mask(m.getName())).orElse("익명");
        return Response.from(r, author);
    }

    /** 김선아 -> 김** */
    private String mask(String name) {
        if (name == null || name.isBlank()) return "익명";
        if (name.length() == 1) return name + "*";
        return name.charAt(0) + "*".repeat(name.length() - 1);
    }
}