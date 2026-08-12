package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.BedAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedAssignmentRepository extends JpaRepository<BedAssignment, Long> {
    List<BedAssignment> findByEncounterId(Long encounterId);
    List<BedAssignment> findByPatientIdAndStatus(Long patientId, String status);
    Optional<BedAssignment> findByBedIdAndStatus(Long bedId, String status);
}
