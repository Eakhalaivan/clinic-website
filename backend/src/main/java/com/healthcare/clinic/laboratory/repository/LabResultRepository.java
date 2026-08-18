package com.healthcare.clinic.laboratory.repository;

import com.healthcare.clinic.laboratory.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LabResultRepository extends JpaRepository<LabResult, Long> {
    List<LabResult> findByRequestPatientIdOrderByEnteredAtDesc(Long patientId);
    Optional<LabResult> findByRequestId(Long requestId);

    @Query(value = "SELECT AVG(DATE_PART('epoch', r.verified_at) - DATE_PART('epoch', req.requested_at))/3600.0 " +
                   "FROM lab_results r JOIN lab_test_requests req ON r.request_id = req.id " +
                   "WHERE r.verified_at IS NOT NULL", nativeQuery = true)
    Double calculateAverageTatHours();
}
