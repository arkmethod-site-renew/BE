package com.arcmethod.community.service;

import com.arcmethod.community.domain.CommunityPost;
import com.arcmethod.community.dto.CommunityDtos.PostRequest;
import com.arcmethod.community.dto.CommunityDtos.PostResponse;
import com.arcmethod.community.repository.CommunityPostRepository;
import com.arcmethod.member.repository.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommunityPostRepository postRepository;
    private final MemberRepository memberRepository;

    public List<PostResponse> findAll(String boardType) {
        List<CommunityPost> posts = (boardType == null || boardType.isBlank())
                ? postRepository.findAllByOrderByCreatedAtDesc()
                : postRepository.findByBoardTypeOrderByCreatedAtDesc(boardType);

        if (posts.isEmpty()) return List.of();

        Map<Long, String> names = memberRepository.findAllById(
                        posts.stream().map(CommunityPost::getMemberId).distinct().toList())
                .stream().collect(Collectors.toMap(m -> m.getId(), m -> mask(m.getName()),
                        (a, b) -> a));

        return posts.stream()
                .map(p -> PostResponse.from(p, names.getOrDefault(p.getMemberId(), "익명")))
                .toList();
    }

    @Transactional
    public PostResponse findOne(Long id) {
        CommunityPost p = get(id);
        p.increaseView();
        String author = memberRepository.findById(p.getMemberId())
                .map(m -> mask(m.getName())).orElse("익명");
        return PostResponse.from(p, author);
    }

    @Transactional
    public PostResponse create(Long memberId, PostRequest req) {
        CommunityPost p = postRepository.save(
                CommunityPost.create(memberId, req.boardType(), req.title(), req.content()));
        String author = memberRepository.findById(memberId)
                .map(m -> mask(m.getName())).orElse("익명");
        return PostResponse.from(p, author);
    }

    private CommunityPost get(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다: " + id));
    }

    private String mask(String name) {
        if (name == null || name.isBlank()) return "익명";
        if (name.length() == 1) return name + "*";
        return name.charAt(0) + "*".repeat(name.length() - 1);
    }
}