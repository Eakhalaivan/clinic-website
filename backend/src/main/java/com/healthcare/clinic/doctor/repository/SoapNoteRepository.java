package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.SoapNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoapNoteRepository extends JpaRepository<SoapNote, Long> {
    Optional<SoapNote> findByEncounterId(Long encounterId);
}
