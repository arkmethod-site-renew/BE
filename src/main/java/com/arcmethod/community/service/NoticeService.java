package com.arcmethod.community.service;

import com.arcmethod.community.domain.Notice;
import com.arcmethod.community.dto.NoticeDtos.Request;
import com.arcmethod.community.dto.NoticeDtos.Response;
import com.arcmethod.community.repository.NoticeRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    /** 관리자: 전부 */
    public List<Response> findAll() {
        return noticeRepository.findAllByOrderByPinnedDescCreatedAtDesc()
                .stream().map(Response::from).toList();
    }
    /** 공개: 노출중인 것만 */
    public List<Response> findLive() {
        return noticeRepository.findAllByOrderByPinnedDescCreatedAtDesc()
                .stream().filter(Notice::isLive).map(Response::from).toList();
    }
    public Response findOne(Long id) {
        return Response.from(get(id));
    }
    @Transactional
    public Response create(Request r) {
        Notice n = Notice.create(r.title(), r.content(), r.categoryOrDefault(),
                r.pinnedOrDefault(), r.activeOrDefault(), r.startsAt(), r.endsAt(), null);
        return Response.from(noticeRepository.save(n));
    }
    @Transactional
    public Response update(Long id, Request r) {
        Notice n = get(id);
        n.update(r.title(), r.content(), r.categoryOrDefault(),
                r.pinnedOrDefault(), r.activeOrDefault(), r.startsAt(), r.endsAt());
        return Response.from(n);
    }
    @Transactional
    public void delete(Long id) {
        noticeRepository.delete(get(id));
    }
    private Notice get(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("공지를 찾을 수 없습니다: " + id));
    }
}