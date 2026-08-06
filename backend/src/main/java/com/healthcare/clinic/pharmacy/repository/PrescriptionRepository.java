package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyPrescriptionRepository")
public interface PrescriptionRepository extends JpaRepository<PharmacyPrescriptionRecord, Long> {
    long countByStatus(String status);
}
