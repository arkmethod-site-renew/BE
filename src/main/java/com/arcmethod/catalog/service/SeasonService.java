package com.arcmethod.catalog.service;
import com.arcmethod.catalog.domain.Season;
import com.arcmethod.catalog.dto.SeasonDtos.Request;
import com.arcmethod.catalog.dto.SeasonDtos.Response;
import com.arcmethod.catalog.repository.SeasonRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeasonService {
    private final SeasonRepository seasonRepository;
    public List<Response> findAll(){
        return seasonRepository.findAllByOrderBySortOrderAscIdAsc()
                .stream().map(Response::from).toList();
    }
    public Response findOne(Long id){
        return Response.from(get(id));
    }
    @Transactional
    public Response create(Request r){
        Season s = Season.create(r.code(), r.name(), r.concept(), r.activeOrDefault(), r.startsAt(), r.endsAt(), r.sortOrDefault());
        return Response.from(seasonRepository.save(s));
    }
    @Transactional
    public Response update(Long id, Request r){
        Season s = get(id);
        s.update(r.code(), r.name(), r.concept(), r.activeOrDefault(), r.startsAt(), r.endsAt(), r.sortOrDefault());
        return Response.from(s);
    }
    @Transactional
    public void delete(Long id){
        seasonRepository.delete(get(id));
    }
    private Season get(Long id){
        return seasonRepository.findById(id).orElseThrow(() -> new NoSuchElementException("시즌을 찾을 수 없습니다: " + id));
    }
}
