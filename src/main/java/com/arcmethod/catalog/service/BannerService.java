package com.arcmethod.catalog.service;
import com.arcmethod.catalog.domain.Banner;
import com.arcmethod.catalog.dto.BannerDtos.Request;
import com.arcmethod.catalog.dto.BannerDtos.Response;
import com.arcmethod.catalog.repository.BannerRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {
    private final BannerRepository bannerRepository;
    //관리자 전부
    public List<Response> findAll(){
        return bannerRepository.findAllByOrderByPositionAscSortOrderAscIdAsc().stream().map(Response::from).toList();
    }
    //공개
    public List<Response> findLive(String position){
        List<Banner> list = (position == null || position.isBlank())
                ?bannerRepository.findAllByOrderByPositionAscSortOrderAscIdAsc()
                :bannerRepository.findByPositionOrderBySortOrderAscIdAsc(position);
        return list.stream().filter(Banner::isLive).map(Response::from).toList();
    }
    public Response findOne(Long id){
        return Response.from(get(id));
    }
    @Transactional
    public Response update(Long id, Request r){
        Banner b = get(id);
        b.update(r.position(), r.title(), r.subtitle(),
                r.imageOrEmpty(), r.mobileImageUrl(), r.linkUrl(), r.colorOrDefault(),
                r.activeOrDefault(), r.startsAt(), r.endsAt(), r.sortOrDefault());
        return Response.from(b);
    }
    @Transactional
    public Response create(Request r){
        Banner b = Banner.create(r.position(), r.title(), r.subtitle(),
                r.imageOrEmpty(), r.mobileImageUrl(), r.linkUrl(), r.colorOrDefault(),
                r.activeOrDefault(), r.startsAt(), r.endsAt(), r.sortOrDefault());
        return Response.from(bannerRepository.save(b));
    }
    @Transactional
    public void delete(Long id){
        bannerRepository.delete(get(id));
    }
    private Banner get(Long id){
        return bannerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("배너를 찾을 수 없습니다: "+ id));
    }
}
