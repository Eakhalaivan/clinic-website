package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;

@Repository("pharmacyPurchaseOrderRepository")
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {

    @EntityGraph(attributePaths = {"supplier", "lineItems", "lineItems.medicine"})
    org.springframework.data.domain.Page<PurchaseOrder> findAll(org.springframework.data.domain.Pageable pageable);

    @EntityGraph(attributePaths = {"supplier", "lineItems", "lineItems.medicine"})
    java.util.Optional<PurchaseOrder> findById(String id);

    List<PurchaseOrder> findByStatus(String status);

    List<PurchaseOrder> findBySupplierIdAndStatus(Long supplierId, String status);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.expectedDeliveryDate < :today AND po.status NOT IN ('completed', 'cancelled')")
    List<PurchaseOrder> findOverduePOs(@Param("today") java.time.LocalDate today);

    long countByStatus(String status);

    @EntityGraph(attributePaths = {"supplier", "lineItems", "lineItems.medicine"})
    org.springframework.data.domain.Page<PurchaseOrder> findByStatus(
            String status, org.springframework.data.domain.Pageable pageable);

    @EntityGraph(attributePaths = {"supplier", "lineItems", "lineItems.medicine"})
    org.springframework.data.domain.Page<PurchaseOrder> findByPoNumberContainingIgnoreCaseOrSupplierNameContainingIgnoreCase(
            String poNumber, String supplierName,
            org.springframework.data.domain.Pageable pageable);
}
