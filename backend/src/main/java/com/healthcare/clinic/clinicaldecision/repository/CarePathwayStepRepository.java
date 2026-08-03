package com.healthcare.clinic.clinicaldecision.repository;

import com.healthcare.clinic.clinicaldecision.entity.CarePathwayStep;
import com.healthcare.clinic.clinicaldecision.entity.StepStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarePathwayStepRepository extends JpaRepository<CarePathwayStep, Long> {
    List<CarePathwayStep> findByPathwayIdOrderByStepNumberAsc(Long pathwayId);

    @Query("SELECT s FROM CarePathwayStep s JOIN s.pathway p WHERE p.assignedByDoctorId = :doctorId AND s.status IN :statuses")
    List<CarePathwayStep> findDoctorPendingSteps(@Param("doctorId") Long doctorId, @Param("statuses") List<StepStatus> statuses);
}
