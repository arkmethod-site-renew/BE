package com.arcmethod.community.controller;
import com.arcmethod.community.dto.CommunityDtos.PostRequest;
import com.arcmethod.community.dto.CommunityDtos.PostResponse;
import com.arcmethod.community.service.CommunityService;
import com.arcmethod.member.repository.MemberRepository;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    private final MemberRepository memberRepository;
    @GetMapping
    public List<PostResponse>list(@RequestParam(required = false)String boardType){
        return communityService.findAll(boardType);
    }
    @GetMapping("/{id}")
    public PostResponse detail(@PathVariable Long id){
        return communityService.findOne(id);
    }
    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest req, Principal principal){
        if(principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Long memberId = memberRepository.findByEmail(principal.getName()).orElseThrow().getId();
        return ResponseEntity.ok(communityService.create(memberId, req));
    }
}
