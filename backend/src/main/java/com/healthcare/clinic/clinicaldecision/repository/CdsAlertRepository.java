package com.healthcare.clinic.clinicaldecision.repository;

import com.healthcare.clinic.clinicaldecision.entity.AlertStatus;
import com.healthcare.clinic.clinicaldecision.entity.CdsAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CdsAlertRepository extends JpaRepository<CdsAlert, Long> {
    List<CdsAlert> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<CdsAlert> findByPatientIdAndStatus(Long patientId, AlertStatus status);
    List<CdsAlert> findByStatusOrderByCreatedAtDesc(AlertStatus status);
}
