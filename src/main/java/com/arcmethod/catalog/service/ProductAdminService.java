package com.arcmethod.catalog.service;
import com.arcmethod.catalog.domain.Product;
import com.arcmethod.catalog.domain.ProductStatus;
import com.arcmethod.catalog.domain.ProductVariant;
import com.arcmethod.catalog.repository.ProductRepository;
import com.arcmethod.catalog.repository.ProductVariantRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductAdminService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    public void changeDiscount(Long productId, short rate){
        product(productId).changeDiscountRate(rate);
    }
    public void changeStatus(Long productId, String status){
        product(productId).changeStatus(ProductStatus.valueOf(status));
    }
    public void changeFlags(Long productId, boolean isNew, boolean isBest){
        product(productId).changeFlags(isNew, isBest);
    }
    public void changeStock(Long variantId, int qty){
        ProductVariant v = variantRepository.findById(variantId).orElseThrow(() -> new NoSuchElementException("변형을 찾을 수 없습니다: " + variantId));
        v.changeStock(qty);
    }
    private Product product(Long id){
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다: "+ id));
    }
}
