package com.arcmethod.catalog.service;
import com.arcmethod.catalog.domain.Category;
import com.arcmethod.catalog.domain.Product;
import com.arcmethod.catalog.domain.Promotion;
import com.arcmethod.catalog.dto.PromotionDtos.Request;
import com.arcmethod.catalog.dto.PromotionDtos.Response;
import com.arcmethod.catalog.repository.CategoryRepository;
import com.arcmethod.catalog.repository.PromotionRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final CategoryRepository categoryRepository;
    public List<Response> findAll(){
        return promotionRepository.findAllByOrderByPriorityDescIdDesc().stream().map(Response::from).toList();
    }
    public Response findOne(Long id){
        return Response.from(get(id));
    }
    public List<Promotion> livePromotions(){
        return promotionRepository.findAllByOrderByPriorityDescIdDesc().stream().filter(Promotion::isLive).toList();
    }
    //상품 적용 최종 할인율
    public short effectiveRate(Product p, List<Promotion> live){
        return live.stream().filter(pr -> pr.appliesTo(p)).map(Promotion::getDiscountRate).findFirst().orElse(p.getDiscountRate());
    }
    @Transactional
    public Response update(Long id, Request r) {
        Promotion p = get(id);
        p.update(r.name(), r.discountRate(), r.scope(), resolveCategory(r.categoryId()), r.activeOrDefault(), r.startsAt(), r.endsAt(), r.priorityOrDefault(), r.productIds());
        return Response.from(p);
    }

    @Transactional
    public Response create(Request r){
        Promotion p = Promotion.create(r.name(), r.discountRate(), r.scope(), resolveCategory(r.categoryId()), r.activeOrDefault(), r.startsAt(), r.endsAt(), r.priorityOrDefault(), r.productIds());
        return Response.from(promotionRepository.save(p));
    }
    @Transactional
    public void delete(Long id){
        promotionRepository.delete(get(id));
    }
    private Category resolveCategory(Long id){
        return id == null ? null : categoryRepository.findById(id).orElse(null);
    }
    private Promotion get(Long id){
        return promotionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("프로모션을 찾을 수 없습니다: "+ id));
    }
    public Promotion matched(Product p, List<Promotion> live){
        return live.stream().filter(pr -> pr.appliesTo(p)).findFirst().orElse(null);
    }
}
