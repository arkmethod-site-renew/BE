package com.arcmethod.order.repository;

import com.arcmethod.order.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    /** 취소 제외 누적 매출 */
    @Query("select coalesce(sum(o.totalAmount), 0) from Order o where o.status <> 'CANCELLED'")
    long sumRevenue();
}