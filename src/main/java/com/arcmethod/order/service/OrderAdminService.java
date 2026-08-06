package com.arcmethod.order.service;
import com.arcmethod.order.domain.Order;
import com.arcmethod.order.dto.OrderAdminDtos.Response;
import com.arcmethod.order.repository.OrderRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderAdminService {
    private final OrderRepository orderRepository;
    public List<Response> findAll(){
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(Response::from).toList();
    }

    /**
     * 내 주문 내역.
     * Response.from()이 LAZY 컬렉션(items)을 읽으므로 반드시 트랜잭션 안에서 매핑해야 한다.
     * (open-in-view=false라 컨트롤러에서 매핑하면 LazyInitializationException)
     */
    public List<Response> findByMember(Long memberId){
        return orderRepository.findByMemberIdOrderByCreatedAtDesc(memberId).stream()
                .map(Response::from).toList();
    }
    @Transactional
    public void changeStatus(Long id, String status){
        Order o = orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("주문을 찾을 수 없습니다: " + id));
        o.changeStatus(status);
    }
}
