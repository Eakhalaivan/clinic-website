package com.healthcare.clinic.radiology.repository;

import com.healthcare.clinic.radiology.entity.ImagingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagingRequestRepository extends JpaRepository<ImagingRequest, Long> {
    List<ImagingRequest> findByStatus(String status);
    List<ImagingRequest> findByPatientId(Long patientId);
    List<ImagingRequest> findAllByOrderByRequestedAtDesc();
    boolean existsByPatientIdAndProcedureIdAndRequestedAtGreaterThanEqual(Long patientId, Long procedureId, java.time.ZonedDateTime requestedAt);
}
