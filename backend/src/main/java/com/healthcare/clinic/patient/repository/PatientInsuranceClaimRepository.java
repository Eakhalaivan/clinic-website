package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.PatientInsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientInsuranceClaimRepository extends JpaRepository<PatientInsuranceClaim, Long> {
    List<PatientInsuranceClaim> findByPatientIdOrderBySubmittedAtDesc(Long patientId);
}
