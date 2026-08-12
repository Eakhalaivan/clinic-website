package com.healthcare.clinic.radiology.repository;

import com.healthcare.clinic.radiology.entity.RadiologyInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RadiologyInventoryItemRepository extends JpaRepository<RadiologyInventoryItem, Long> {

    Optional<RadiologyInventoryItem> findBySkuAndBranchId(String sku, Long branchId);

    List<RadiologyInventoryItem> findByBranchId(Long branchId);
}
