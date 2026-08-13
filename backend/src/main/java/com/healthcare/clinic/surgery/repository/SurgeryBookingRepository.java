package com.healthcare.clinic.surgery.repository;

import com.healthcare.clinic.surgery.entity.SurgeryBooking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SurgeryBookingRepository extends JpaRepository<SurgeryBooking, Long> {
    
    @Query("SELECT s FROM SurgeryBooking s JOIN s.operationTheatre o WHERE o.branchId = :branchId")
    List<SurgeryBooking> findByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT s FROM SurgeryBooking s JOIN s.operationTheatre o WHERE o.branchId = :branchId AND s.status = :status")
    List<SurgeryBooking> findByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") String status);

    @Query("SELECT s FROM SurgeryBooking s WHERE s.operationTheatre.id = :otId AND s.status = 'SCHEDULED' AND " +
           "((s.scheduledStartTime >= :startTime AND s.scheduledStartTime < :endTime) OR " +
           "(s.scheduledStartTime < :startTime AND " + // Need to handle duration overlap properly in service or here.
           " s.scheduledStartTime > :endTime))") // This is simplified. Service layer will do robust check if needed.
    List<SurgeryBooking> findOverlappingBookings(@Param("otId") Long otId, @Param("startTime") ZonedDateTime startTime, @Param("endTime") ZonedDateTime endTime);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM SurgeryBooking s WHERE s.id = :id")
    Optional<SurgeryBooking> findByIdWithLock(@Param("id") Long id);
}
