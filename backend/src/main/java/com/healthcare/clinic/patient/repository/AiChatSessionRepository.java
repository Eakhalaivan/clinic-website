package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.AiChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiChatSessionRepository extends JpaRepository<AiChatSession, Long> {
    Optional<AiChatSession> findFirstByPatientIdAndStatusOrderByCreatedAtDesc(Long patientId, String status);
}
