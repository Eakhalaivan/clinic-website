package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QueueTokenRepository extends JpaRepository<QueueToken, Long> {
    
    @Query("SELECT MAX(q.tokenNumber) FROM QueueToken q WHERE q.branch.id = :branchId AND q.generatedAt >= :startOfDay")
    Optional<Integer> findMaxTokenForBranchToday(@Param("branchId") Long branchId, @Param("startOfDay") ZonedDateTime startOfDay);

    List<QueueToken> findByBranchIdAndStatusOrderByTokenNumberAsc(Long branchId, String status);
    
    List<QueueToken> findByAppointmentId(Long appointmentId);
    
    List<QueueToken> findByBranchIdAndGeneratedAtAfterOrderByPriorityLevelDescTokenNumberAsc(Long branchId, ZonedDateTime generatedAt);

    long countByBranchIdAndStatus(Long branchId, String status);
}
