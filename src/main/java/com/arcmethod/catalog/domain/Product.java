package com.arcmethod.catalog.domain;
import com.arcmethod.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="category_id", nullable = false)
    private Category category;
    @Column(nullable = false, length = 200)
    private String name;
    @Column(nullable = false, length = 220, unique = true)
    private String slug;
    @Column(columnDefinition = "text")
    private String description;
    @Column(nullable = false)
    private int price;
    @Column(name = "discount_rate", nullable =false)
    private short discountRate;
    @Enumerated(EnumType.STRING)
    @Column(nullable =false, length = 20)
    private ProductStatus status;
    @Column(name = "is_new", nullable = false)
    private boolean isNew;
    @Column(name = "is_best", nullable = false)
    private boolean isBest;
    @Column(name = "preorder_ship_date")
    private LocalDate preorderShipDate;
    @Column(length = 255)
    private String material;
    @Column(name ="care_instructions", length = 500)
    private String careInstructions;
    @Column(length = 20)private String thickness;
    @Column(length = 20)private String elasticity;
    @Column(length = 20)private String transparency;
    @Column(length = 20)private String lining;
    @Column(length = 20)private String season;
    @Column(name = "model_height_cm") private Short modelHeightCm;
    @Column(name = "model_weight_kg")private Short modelWeightKg;
    @Column(name = "model_size_worn", length = 20)private String modelSizeWorn;
    public int salePrice(){
        return Math.round(price * (100 - discountRate)/ 100.0f);
    }
    //관리자용
    public void changeDiscountRate(short rate){
        if(rate < 0 || rate > 100){
            throw new IllegalArgumentException("할인율은 0~100 사이어야 합니다:" + rate);
        }
        this.discountRate = rate;
    }
    public void changeStatus(ProductStatus status){
        this.status = status;
    }
    public void changeFlags(boolean isNew, boolean isBest){
        this.isNew = isNew;
        this.isBest = isBest;
    }
}
