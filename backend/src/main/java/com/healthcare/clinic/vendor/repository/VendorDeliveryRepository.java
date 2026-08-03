package com.healthcare.clinic.vendor.repository;

import com.healthcare.clinic.vendor.entity.VendorDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorDeliveryRepository extends JpaRepository<VendorDelivery, Long> {
    List<VendorDelivery> findByVendorUserId(Long vendorUserId);
    List<VendorDelivery> findByPurchaseOrderId(Long poId);
}
