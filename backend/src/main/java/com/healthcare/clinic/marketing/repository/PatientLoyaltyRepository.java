package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.PatientLoyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientLoyaltyRepository extends JpaRepository<PatientLoyalty, Long> {
    Optional<PatientLoyalty> findByPatientId(Long patientId);
}
