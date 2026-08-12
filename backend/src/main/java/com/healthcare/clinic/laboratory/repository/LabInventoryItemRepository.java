package com.healthcare.clinic.laboratory.repository;

import com.healthcare.clinic.laboratory.entity.LabInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabInventoryItemRepository extends JpaRepository<LabInventoryItem, Long> {
    Optional<LabInventoryItem> findBySku(String sku);
}
