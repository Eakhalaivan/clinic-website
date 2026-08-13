package com.healthcare.clinic.surgery.repository;

import com.healthcare.clinic.surgery.entity.AnesthesiaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnesthesiaRecordRepository extends JpaRepository<AnesthesiaRecord, Long> {
    Optional<AnesthesiaRecord> findBySurgeryBookingId(Long surgeryBookingId);
}
