package com.arcmethod.member.service;
import com.arcmethod.member.domain.Member;
import com.arcmethod.member.dto.MemberAdminDtos.Response;
import com.arcmethod.member.repository.MemberRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAdminService {
    private final MemberRepository memberRepository;
    public List<Response> findAll(){
        return memberRepository.findAllByOrderByCreatedAtDesc().stream().map(Response::from).toList();
    }
    @Transactional
    public void changeStatus(Long id, String status){
        get(id).updateStatus(status);
    }
    @Transactional
    public void changeMemo(Long id, String memo){
        get(id).updateMemo(memo);
    }
    private Member get(Long id){
        return memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다: " + id));
    }
}
