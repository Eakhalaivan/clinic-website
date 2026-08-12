package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.ClinicalAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalAttachmentRepository extends JpaRepository<ClinicalAttachment, Long> {
    List<ClinicalAttachment> findByPatientId(Long patientId);
    List<ClinicalAttachment> findByEncounterId(Long encounterId);
}
