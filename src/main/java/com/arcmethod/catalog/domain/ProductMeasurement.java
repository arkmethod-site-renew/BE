package com.arcmethod.catalog.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product_measurement")
public class ProductMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable =false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;
    @Column(name = "item_key", nullable = false, length = 40)
    private String itemKey;
    @Column(name = "value_cm", nullable = false, precision = 5, scale = 1)
    private BigDecimal valueCm;
}
