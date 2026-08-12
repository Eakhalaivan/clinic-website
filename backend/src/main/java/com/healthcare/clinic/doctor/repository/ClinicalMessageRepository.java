package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.ClinicalMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalMessageRepository extends JpaRepository<ClinicalMessage, Long> {
    List<ClinicalMessage> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
    List<ClinicalMessage> findBySenderIdOrderByCreatedAtDesc(Long senderId);
    List<ClinicalMessage> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
