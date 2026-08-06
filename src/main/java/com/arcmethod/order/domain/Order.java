package com.arcmethod.order.domain;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    @Column(name = "order_no", nullable = false, length = 30, unique = true)
    private String orderNo;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(name = "total_amount", nullable = false)
    private int totalAmount;
    @Column(name = "receiver_name", length = 50)
    private String receiverName;
    @Column(name = "receiver_phone", length = 20)
    private String receiverPhone;
    @Column(length = 300)
    private String address;
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();
    public void changeStatus(String status){
        this.status = status;
    }
    public static Order create(Long memberId, String orderNo, String receiverName, String receiverPhone, String address){
        Order o = new Order();
        o.memberId = memberId;
        o.orderNo = orderNo;
        o.status = "PENDING";
        o.totalAmount = 0;
        o.receiverName = receiverName;
        o.receiverPhone = receiverPhone;
        o.address = address;
        return o;
    }
    public void applyTotal(int total){this.totalAmount = total;}
}
