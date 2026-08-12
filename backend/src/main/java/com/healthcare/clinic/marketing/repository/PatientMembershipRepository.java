package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.PatientMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientMembershipRepository extends JpaRepository<PatientMembership, Long> {
    List<PatientMembership> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    Optional<PatientMembership> findTopByPatientIdAndStatusOrderByEndDateDesc(Long patientId, String status);
}
