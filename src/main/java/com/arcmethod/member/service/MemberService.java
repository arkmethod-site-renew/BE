package com.arcmethod.member.service;

import com.arcmethod.member.domain.Member;
import com.arcmethod.member.dto.AuthDtos.MeResponse;
import com.arcmethod.member.dto.RegisterDtos.Request;
import com.arcmethod.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MeResponse register(Request req) {
        memberRepository.findByEmail(req.email()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        });
        Member m = Member.signUp(
                req.email(),
                passwordEncoder.encode(req.password()),   // {bcrypt}... 형태로 저장
                req.name(),
                req.phone());

        return MeResponse.from(memberRepository.save(m));
    }
}