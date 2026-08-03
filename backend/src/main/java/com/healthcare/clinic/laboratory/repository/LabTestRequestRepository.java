package com.healthcare.clinic.laboratory.repository;

import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabTestRequestRepository extends JpaRepository<LabTestRequest, Long> {
    List<LabTestRequest> findByStatus(String status);
    List<LabTestRequest> findByPatientIdOrderByRequestedAtDesc(Long patientId);
    List<LabTestRequest> findByDoctorUserIdOrderByRequestedAtDesc(Long doctorUserId);
}
