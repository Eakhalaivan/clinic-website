package com.healthcare.clinic.radiology.repository;

import com.healthcare.clinic.radiology.entity.RadiologyAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RadiologyAccessLogRepository extends JpaRepository<RadiologyAccessLog, Long> {
}
