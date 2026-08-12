package com.healthcare.clinic.surgery.repository;

import com.healthcare.clinic.surgery.entity.SurgeryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurgeryNoteRepository extends JpaRepository<SurgeryNote, Long> {
    Optional<SurgeryNote> findBySurgeryBookingId(Long surgeryBookingId);
}
