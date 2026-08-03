package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.PharmacyPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyPatientRepository")
public interface PharmacyPatientRepository extends JpaRepository<PharmacyPatient, Long> {
    List<PharmacyPatient> findByNameContainingIgnoreCaseOrUhidContainingIgnoreCase(String name, String uhid);
}
