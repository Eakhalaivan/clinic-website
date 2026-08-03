package com.healthcare.clinic.clinicaldecision.repository;

import com.healthcare.clinic.clinicaldecision.entity.PatientCarePathway;
import com.healthcare.clinic.clinicaldecision.entity.PathwayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientCarePathwayRepository extends JpaRepository<PatientCarePathway, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"steps"})
    java.util.Optional<PatientCarePathway> findWithStepsById(Long id);

    List<PatientCarePathway> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<PatientCarePathway> findByPatientIdAndStatus(Long patientId, PathwayStatus status);
    List<PatientCarePathway> findByAssignedByDoctorIdAndStatus(Long doctorId, PathwayStatus status);
}
