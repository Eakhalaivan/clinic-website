package com.healthcare.clinic.analytics.ipd;

import com.healthcare.clinic.inpatient.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface IpdAnalyticsRepository extends JpaRepository<Admission, Long> {

    @Query(value = "SELECT CAST(i.admitted_at AS DATE) as metricDate, COUNT(i.id) as total " +
            "FROM admissions i " +
            "WHERE (:branchId IS NULL OR i.branch_id = :branchId) " +
            "AND i.admitted_at >= :startDate AND i.admitted_at <= :endDate " +
            "GROUP BY CAST(i.admitted_at AS DATE) " +
            "ORDER BY metricDate ASC", nativeQuery = true)
    List<Object[]> getDailyAdmissionsVolume(@Param("branchId") Long branchId,
                                            @Param("startDate") ZonedDateTime startDate,
                                            @Param("endDate") ZonedDateTime endDate);

    @Query(value = "SELECT b.ward as wardName, COUNT(b.id) as total " +
            "FROM beds b " +
            "WHERE b.status = 'OCCUPIED' " +
            "GROUP BY b.ward " +
            "ORDER BY total DESC", nativeQuery = true)
    List<Object[]> getCurrentWardOccupancy();

    @Query(value = "SELECT COUNT(i.id) FROM admissions i " +
            "WHERE (:branchId IS NULL OR i.branch_id = :branchId) " +
            "AND i.status = :status", nativeQuery = true)
    long countAdmissionsByStatus(@Param("branchId") Long branchId, @Param("status") String status);
}
