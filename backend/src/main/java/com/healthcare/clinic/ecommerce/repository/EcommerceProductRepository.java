package com.healthcare.clinic.ecommerce.repository;

import com.healthcare.clinic.ecommerce.entity.EcommerceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcommerceProductRepository extends JpaRepository<EcommerceProduct, Long> {
    List<EcommerceProduct> findByIsActiveTrue();
    List<EcommerceProduct> findByCategory(String category);
}
