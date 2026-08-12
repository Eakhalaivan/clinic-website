package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.TeleconsultationSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeleconsultationSessionRepository extends JpaRepository<TeleconsultationSession, Long> {
    Optional<TeleconsultationSession> findByEncounterId(Long encounterId);
}
