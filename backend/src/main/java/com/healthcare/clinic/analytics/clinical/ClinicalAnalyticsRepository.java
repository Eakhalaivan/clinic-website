package com.healthcare.clinic.analytics.clinical;

import com.healthcare.clinic.doctor.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ClinicalAnalyticsRepository extends JpaRepository<Prescription, Long> {

    @Query(value = "SELECT f.status as status, COUNT(f.id) as total " +
            "FROM doctor_followups f " +
            "JOIN appointments a ON f.appointment_id = a.id " +
            "JOIN appointment_slots s ON a.slot_id = s.id " +
            "WHERE (:branchId IS NULL OR a.branch_id = :branchId) " +
            "AND s.start_time >= :startDate AND s.start_time <= :endDate " +
            "GROUP BY f.status", nativeQuery = true)
    List<Object[]> getFollowUpCompletionRates(@Param("branchId") Long branchId,
                                              @Param("startDate") ZonedDateTime startDate,
                                              @Param("endDate") ZonedDateTime endDate);

    @Query(value = "SELECT CAST(p.created_at AS DATE) as metricDate, COUNT(p.id) as total " +
            "FROM prescriptions p " +
            "JOIN appointments a ON p.appointment_id = a.id " +
            "WHERE (:branchId IS NULL OR a.branch_id = :branchId) " +
            "AND p.created_at >= :startDate AND p.created_at <= :endDate " +
            "GROUP BY CAST(p.created_at AS DATE) " +
            "ORDER BY metricDate ASC", nativeQuery = true)
    List<Object[]> getDailyPrescriptionVolume(@Param("branchId") Long branchId,
                                              @Param("startDate") ZonedDateTime startDate,
                                              @Param("endDate") ZonedDateTime endDate);

}
