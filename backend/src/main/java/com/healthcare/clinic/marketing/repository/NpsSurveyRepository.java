package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.NpsSurvey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NpsSurveyRepository extends JpaRepository<NpsSurvey, Long> {
    Optional<NpsSurvey> findByIdempotencyKey(String idempotencyKey);
    Page<NpsSurvey> findByBranchIdOrderByCreatedAtDesc(Long branchId, Pageable pageable);
    Page<NpsSurvey> findByPatientIdOrderByCreatedAtDesc(Long patientId, Pageable pageable);
    long countByStatus(String status);
}
