package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.PatientDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientDocumentRepository extends JpaRepository<PatientDocument, Long> {
    List<PatientDocument> findByPatientIdOrderByUploadedAtDesc(Long patientId);
}
