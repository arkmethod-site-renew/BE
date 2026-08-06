package com.arcmethod.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Column(name = "variant_id", nullable =false)
    private Long variantId;
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;
    @Column(name = "color_name", nullable = false, length = 40)
    private String colorName;
    @Column(name = "size_name", nullable = false, length = 20)
    private String sizeName;
    @Column(name = "unit_price", nullable = false)
    private int unitPrice;
    @Column(nullable = false)
    private int quantity;
    public static OrderItem create(Order order, Long variantId, String productName, String colorName, String sizeName, int unitPrice, int quantity){
        OrderItem i = new OrderItem();
        i.order = order;
        i.variantId = variantId;
        i.productName = productName;
        i.colorName = colorName;
        i.sizeName = sizeName;
        i.unitPrice = unitPrice;
        i.quantity = quantity;
        return i;
    }
}
