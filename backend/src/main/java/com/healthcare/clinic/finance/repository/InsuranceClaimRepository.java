package com.healthcare.clinic.finance.repository;

import com.healthcare.clinic.finance.entity.InsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {
    List<InsuranceClaim> findByPatientId(Long patientId);
    List<InsuranceClaim> findByStatus(String status);
    List<InsuranceClaim> findAllByOrderBySubmittedAtDesc();
}
