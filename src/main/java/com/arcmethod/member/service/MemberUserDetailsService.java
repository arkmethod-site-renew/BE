package com.arcmethod.member.service;

import com.arcmethod.member.domain.Member;
import com.arcmethod.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member m = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("계정을 찾을 수 없습니다: " + email));
        return new User(
                m.getEmail(),
                m.getPasswordHash(),
                "ACTIVE".equals(m.getStatus()),
                true,true,true,
                List.of(new SimpleGrantedAuthority("ROLE_" + m.getRole())));
    }
}
