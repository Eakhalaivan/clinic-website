package com.healthcare.clinic.backoffice.inventory.repository;

import com.healthcare.clinic.backoffice.inventory.entity.BackofficePurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackofficePurchaseOrderRepository extends JpaRepository<BackofficePurchaseOrder, Long> {
    List<BackofficePurchaseOrder> findByStatus(String status);
    List<BackofficePurchaseOrder> findAllByOrderByOrderDateDesc();
}
