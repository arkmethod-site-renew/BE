package com.arcmethod.member.controller;
import com.arcmethod.member.domain.Member;
import com.arcmethod.member.dto.AuthDtos.LoginRequest;
import com.arcmethod.member.dto.AuthDtos.MeResponse;
import com.arcmethod.member.dto.RegisterDtos;
import com.arcmethod.member.repository.MemberRepository;
import com.arcmethod.member.service.MemberService;
import com.arcmethod.review.dto.ReviewDtos;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final HttpSessionSecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
    private final MemberService memberService;
    @PostMapping("/login")
    @Transactional
    public ResponseEntity<MeResponse> login(
            @Valid @RequestBody LoginRequest req,
            HttpServletRequest request,
            HttpServletResponse response){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        // 세션 인증
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        contextRepository.saveContext(context, request, response);
        Member m = memberRepository.findByEmail(req.email()).orElseThrow();
        m.markLoggedIn();
        return ResponseEntity.ok(MeResponse.from(m));
    }
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Principal principal){
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return memberRepository.findByEmail(principal.getName())
                .map(m -> ResponseEntity.ok(MeResponse.from(m)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    @PostMapping("/register")
    public ResponseEntity<MeResponse> register(
            @Valid @RequestBody com.arcmethod.member.dto.RegisterDtos.Request req) {
        return ResponseEntity.ok(memberService.register(req));
    }
}
