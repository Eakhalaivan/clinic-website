package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.PatientLoyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface PatientLoyaltyRepository extends JpaRepository<PatientLoyalty, Long> {
    Optional<PatientLoyalty> findByPatientId(Long patientId);

    /** Pessimistic write lock for concurrent redemption safety */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PatientLoyalty> findWithLockByPatientId(Long patientId);
}
