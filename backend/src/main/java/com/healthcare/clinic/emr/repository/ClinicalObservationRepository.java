package com.healthcare.clinic.emr.repository;

import com.healthcare.clinic.emr.entity.ClinicalObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClinicalObservationRepository extends JpaRepository<ClinicalObservation, Long> {
    List<ClinicalObservation> findByPatientId(Long patientId);
}

