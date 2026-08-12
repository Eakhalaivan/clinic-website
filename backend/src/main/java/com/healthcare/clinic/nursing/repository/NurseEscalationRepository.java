package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.NurseEscalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NurseEscalationRepository extends JpaRepository<NurseEscalation, Long> {
    List<NurseEscalation> findByPatientIdOrderByEscalatedAtDesc(Long patientId);
    List<NurseEscalation> findByNurseIdOrderByEscalatedAtDesc(Long nurseId);
    List<NurseEscalation> findByStatusOrderByEscalatedAtDesc(String status);
}
