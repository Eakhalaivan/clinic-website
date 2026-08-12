package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.PatientIdentityVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientIdentityVerificationRepository extends JpaRepository<PatientIdentityVerification, Long> {
    List<PatientIdentityVerification> findByPatientIdOrderByVerifiedAtDesc(Long patientId);
}
