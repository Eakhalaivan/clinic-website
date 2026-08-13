package com.healthcare.clinic.surgery.repository;

import com.healthcare.clinic.surgery.entity.PreOpChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreOpChecklistRepository extends JpaRepository<PreOpChecklist, Long> {
    Optional<PreOpChecklist> findBySurgeryBookingId(Long surgeryBookingId);
}
