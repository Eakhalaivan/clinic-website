package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.InsuranceVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceVerificationRepository extends JpaRepository<InsuranceVerification, Long> {
    List<InsuranceVerification> findByPatientId(Long patientId);
}
