package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<Prescription> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
    List<Prescription> findBySignedAtAfterAndPharmacyStatusNot(java.time.LocalDateTime date, String status);
    List<Prescription> findByEncounterId(Long encounterId);
}
