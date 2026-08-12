package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.pharmacy.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    List<InventoryMovement> findByMedicineIdOrderByCreatedAtDesc(Long medicineId);
    List<InventoryMovement> findByBatchIdOrderByCreatedAtDesc(String batchId);
}
