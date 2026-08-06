package com.arcmethod.catalog.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product_variant")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable =false)
    private Color color;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;
    @Column(nullable = false, length = 60, unique = true)
    private String sku;
    @Column(name = "stock_qty", nullable = false)
    private int stockQty;
    @Column(name = "additional_price", nullable = false)
    private int additionalPrice;
    public void changeStock(int qty){
        if(qty < 0){
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다: " + qty);
        }
        this.stockQty = qty;
    }
}
