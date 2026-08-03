package com.healthcare.clinic.ecommerce.repository;

import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcommerceOrderRepository extends JpaRepository<EcommerceOrder, Long> {
    List<EcommerceOrder> findByUserId(Long userId);
    List<EcommerceOrder> findByStatus(String status);
    List<EcommerceOrder> findAllByOrderByCreatedAtDesc();
}
