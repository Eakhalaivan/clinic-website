package com.healthcare.clinic.radiology.repository;

import com.healthcare.clinic.radiology.entity.RadiologyAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface RadiologyAppointmentRepository extends JpaRepository<RadiologyAppointment, Long> {

    List<RadiologyAppointment> findByBranchIdAndScheduledTimeBetween(Long branchId, ZonedDateTime start, ZonedDateTime end);

    @Query("SELECT a FROM RadiologyAppointment a WHERE a.branch.id = :branchId AND a.roomOrMachine = :machine AND " +
           "((a.scheduledTime >= :start AND a.scheduledTime < :end) OR " +
           "(FUNCTION('DATEADD', MINUTE, a.durationMinutes, a.scheduledTime) > :start AND a.scheduledTime <= :start)) AND " +
           "a.status NOT IN ('CANCELLED', 'NO_SHOW')")
    List<RadiologyAppointment> findOverlappingAppointments(
            @Param("branchId") Long branchId, 
            @Param("machine") String machine, 
            @Param("start") ZonedDateTime start, 
            @Param("end") ZonedDateTime end);

    Optional<RadiologyAppointment> findByRequestId(Long requestId);
}
