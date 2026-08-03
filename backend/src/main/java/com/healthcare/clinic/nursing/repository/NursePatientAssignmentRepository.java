package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.NursePatientAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NursePatientAssignmentRepository extends JpaRepository<NursePatientAssignment, Long> {
    boolean existsByNurseIdAndPatientIdAndStatus(Long nurseId, Long patientId, String status);
    List<NursePatientAssignment> findByNurseIdAndStatus(Long nurseId, String status);
}
