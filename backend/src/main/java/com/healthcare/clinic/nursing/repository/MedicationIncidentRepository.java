package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.MedicationIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationIncidentRepository extends JpaRepository<MedicationIncident, Long> {
    List<MedicationIncident> findByPatientIdOrderByIncidentTimeDesc(Long patientId);
    List<MedicationIncident> findByNurseIdOrderByIncidentTimeDesc(Long nurseId);
    List<MedicationIncident> findByStatusOrderByIncidentTimeDesc(String status);
}
