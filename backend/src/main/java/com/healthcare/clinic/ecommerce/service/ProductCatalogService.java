package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcommerceProduct;
import com.healthcare.clinic.ecommerce.repository.EcommerceProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCatalogService {

    private final EcommerceProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<EcommerceProduct> getActiveProducts() {
        return productRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()) && "ACTIVE".equals(p.getProductStatus()))
                .toList();
    }

    @Transactional(readOnly = true)
    public EcommerceProduct getProductDetails(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Transactional
    public EcommerceProduct createOrUpdateProduct(EcommerceProduct product) {
        // Prevent duplicate SKU
        if (product.getSku() != null && !product.getSku().isBlank()) {
            productRepository.findAll().stream()
                    .filter(p -> product.getSku().equals(p.getSku()) && !p.getId().equals(product.getId()))
                    .findFirst()
                    .ifPresent(p -> { throw new IllegalArgumentException("Duplicate SKU"); });
        }
        
        // Prevent duplicate Barcode
        if (product.getBarcode() != null && !product.getBarcode().isBlank()) {
            productRepository.findAll().stream()
                    .filter(p -> product.getBarcode().equals(p.getBarcode()) && !p.getId().equals(product.getId()))
                    .findFirst()
                    .ifPresent(p -> { throw new IllegalArgumentException("Duplicate Barcode"); });
        }

        if (product.getId() == null) {
            product.setCreatedAt(ZonedDateTime.now());
            if ("ACTIVE".equals(product.getProductStatus())) {
                product.setActivatedAt(ZonedDateTime.now());
            }
        } else {
            EcommerceProduct existing = productRepository.findById(product.getId()).orElseThrow();
            if (!"ACTIVE".equals(existing.getProductStatus()) && "ACTIVE".equals(product.getProductStatus())) {
                product.setActivatedAt(ZonedDateTime.now());
            }
        }

        return productRepository.save(product);
    }

    @Transactional
    public void archiveProduct(Long id) {
        EcommerceProduct product = productRepository.findById(id).orElseThrow();
        product.setProductStatus("ARCHIVED");
        product.setIsActive(false);
        productRepository.save(product);
    }
}
