package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.ClinicalEncounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalEncounterRepository extends JpaRepository<ClinicalEncounter, Long> {
    List<ClinicalEncounter> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<ClinicalEncounter> findByDoctorIdAndStatusNot(Long doctorId, String status);
    List<ClinicalEncounter> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
}
