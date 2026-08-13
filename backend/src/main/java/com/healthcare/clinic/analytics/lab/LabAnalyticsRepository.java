package com.healthcare.clinic.analytics.lab;

import com.healthcare.clinic.laboratory.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface LabAnalyticsRepository extends JpaRepository<LabResult, Long> {

    @Query(value = "SELECT CAST(req.requested_at AS DATE) as metricDate, COUNT(req.id) as total " +
            "FROM lab_test_requests req " +
            "JOIN patient_profiles p ON req.patient_id = p.id " +
            "WHERE (:branchId IS NULL OR p.branch_id = :branchId) " +
            "AND req.requested_at >= :startDate AND req.requested_at <= :endDate " +
            "GROUP BY CAST(req.requested_at AS DATE) " +
            "ORDER BY metricDate ASC", nativeQuery = true)
    List<Object[]> getDailyTestVolume(@Param("branchId") Long branchId,
                                      @Param("startDate") ZonedDateTime startDate,
                                      @Param("endDate") ZonedDateTime endDate);

    @Query(value = "SELECT cat.test_name as testName, COUNT(req.id) as total " +
            "FROM lab_test_requests req " +
            "JOIN patient_profiles p ON req.patient_id = p.id " +
            "JOIN lab_test_catalog cat ON req.test_catalog_id = cat.id " +
            "WHERE (:branchId IS NULL OR p.branch_id = :branchId) " +
            "AND req.requested_at >= :startDate AND req.requested_at <= :endDate " +
            "GROUP BY cat.test_name " +
            "ORDER BY total DESC", nativeQuery = true)
    List<Object[]> getVolumeByTestType(@Param("branchId") Long branchId,
                                       @Param("startDate") ZonedDateTime startDate,
                                       @Param("endDate") ZonedDateTime endDate);

    @Query(value = "SELECT COUNT(res.id) as total, SUM(CASE WHEN res.is_abnormal = true THEN 1 ELSE 0 END) as abnormal " +
            "FROM lab_results res " +
            "JOIN lab_test_requests req ON res.request_id = req.id " +
            "JOIN patient_profiles p ON req.patient_id = p.id " +
            "WHERE (:branchId IS NULL OR p.branch_id = :branchId) " +
            "AND res.entered_at >= :startDate AND res.entered_at <= :endDate", nativeQuery = true)
    List<Object[]> getAbnormalityStats(@Param("branchId") Long branchId,
                                       @Param("startDate") ZonedDateTime startDate,
                                       @Param("endDate") ZonedDateTime endDate);
}
