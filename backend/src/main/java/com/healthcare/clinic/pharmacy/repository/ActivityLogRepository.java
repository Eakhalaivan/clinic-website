package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.pharmacy.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("pharmacyActivityLogRepository")
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    org.springframework.data.domain.Page<ActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<ActivityLog> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, LocalDateTime start, LocalDateTime end, org.springframework.data.domain.Pageable pageable);
}
