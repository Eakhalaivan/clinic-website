package com.healthcare.clinic.emr.repository;

import com.healthcare.clinic.emr.entity.Immunization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmunizationRepository extends JpaRepository<Immunization, Long> {
    List<Immunization> findByPatientId(Long patientId);
}
