package com.healthcare.clinic.analytics.opd;

import com.healthcare.clinic.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface OpdAnalyticsRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "SELECT CAST(s.start_time AS DATE) as metricDate, a.status as status, COUNT(a.id) as total " +
            "FROM appointments a " +
            "JOIN appointment_slots s ON a.slot_id = s.id " +
            "WHERE (:branchId IS NULL OR a.branch_id = :branchId) " +
            "AND s.start_time >= :startDate AND s.start_time <= :endDate " +
            "GROUP BY CAST(s.start_time AS DATE), a.status " +
            "ORDER BY metricDate ASC", nativeQuery = true)
    List<Object[]> getDailyVolumeByStatus(@Param("branchId") Long branchId,
                                          @Param("startDate") ZonedDateTime startDate,
                                          @Param("endDate") ZonedDateTime endDate);

    @Query(value = "SELECT d.specialty as specialty, COUNT(a.id) as total " +
            "FROM appointments a " +
            "JOIN doctor_profiles d ON a.doctor_id = d.id " +
            "JOIN appointment_slots s ON a.slot_id = s.id " +
            "WHERE (:branchId IS NULL OR a.branch_id = :branchId) " +
            "AND s.start_time >= :startDate AND s.start_time <= :endDate " +
            "GROUP BY d.specialty " +
            "ORDER BY total DESC", nativeQuery = true)
    List<Object[]> getVolumeBySpecialty(@Param("branchId") Long branchId,
                                        @Param("startDate") ZonedDateTime startDate,
                                        @Param("endDate") ZonedDateTime endDate);

}
