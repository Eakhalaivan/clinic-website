package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.PharmacyAdvance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;

import java.util.Optional;

@Repository("pharmacyPharmacyAdvanceRepository")
public interface PharmacyAdvanceRepository extends JpaRepository<PharmacyAdvance, Long> {
    Optional<PharmacyAdvance> findByPatientName(String patientName);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PharmacyAdvance> findByPatientId(Long patientId);
}
