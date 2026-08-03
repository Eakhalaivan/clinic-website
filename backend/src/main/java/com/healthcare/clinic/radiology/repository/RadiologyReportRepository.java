package com.healthcare.clinic.radiology.repository;

import com.healthcare.clinic.radiology.entity.RadiologyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RadiologyReportRepository extends JpaRepository<RadiologyReport, Long> {
    Optional<RadiologyReport> findByRequestId(Long requestId);
}
