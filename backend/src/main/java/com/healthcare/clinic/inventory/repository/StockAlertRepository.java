package com.healthcare.clinic.inventory.repository;

import com.healthcare.clinic.inventory.entity.StockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository("pharmacyStockAlertRepository")
public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {
    Optional<StockAlert> findTopByMedicineIdAndCreatedAtAfterOrderByCreatedAtDesc(Long medicineId, LocalDateTime after);
}
