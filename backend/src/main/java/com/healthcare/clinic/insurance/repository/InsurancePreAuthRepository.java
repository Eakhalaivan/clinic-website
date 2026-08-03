package com.healthcare.clinic.insurance.repository;

import com.healthcare.clinic.insurance.entity.InsurancePreAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsurancePreAuthRepository extends JpaRepository<InsurancePreAuth, Long> {
    List<InsurancePreAuth> findByPatientId(Long patientId);
    List<InsurancePreAuth> findByStatus(String status);
    List<InsurancePreAuth> findAllByOrderBySubmittedAtDesc();
}
