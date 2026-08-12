package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.KioskCheckin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface KioskCheckinRepository extends JpaRepository<KioskCheckin, Long> {

    List<KioskCheckin> findByBranchIdAndCreatedAtAfterOrderByCreatedAtDesc(Long branchId, ZonedDateTime since);

    List<KioskCheckin> findByPatientId(Long patientId);

    @Query("SELECT COUNT(k) FROM KioskCheckin k WHERE k.branchId = :branchId AND k.status = :status AND k.createdAt >= :since")
    long countByBranchIdAndStatusAndCreatedAtAfter(Long branchId, String status, ZonedDateTime since);
}
