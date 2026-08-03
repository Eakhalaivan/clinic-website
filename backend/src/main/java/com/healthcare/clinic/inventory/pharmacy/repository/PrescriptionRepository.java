package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.PharmacyPrescriptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyPrescriptionRepository")
public interface PrescriptionRepository extends JpaRepository<PharmacyPrescriptionRecord, Long> {
    long countByStatus(String status);
}
