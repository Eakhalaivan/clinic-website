package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.NarcoticMonthlyReconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pharmacyNarcoticMonthlyReconciliationRepository")
public interface NarcoticMonthlyReconciliationRepository extends JpaRepository<NarcoticMonthlyReconciliation, String> {
    Optional<NarcoticMonthlyReconciliation> findByMedicineIdAndReconciliationMonthAndReconciliationYear(Long medicineId, int month, int year);
}
