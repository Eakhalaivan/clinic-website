package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.DependentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DependentProfileRepository extends JpaRepository<DependentProfile, Long> {
    List<DependentProfile> findByGuardianId(Long guardianId);
}
