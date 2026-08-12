package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.PatientMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientMembershipRepository extends JpaRepository<PatientMembership, Long> {
    List<PatientMembership> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    Optional<PatientMembership> findByPatientIdAndPlanIdAndStatus(Long patientId, Long planId, String status);
    List<PatientMembership> findByStatusAndEndDateBefore(String status, LocalDate date);
    long countByStatus(String status);
}
