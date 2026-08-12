package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.ConsentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsentVersionRepository extends JpaRepository<ConsentVersion, Long> {
    Optional<ConsentVersion> findByConsentTypeAndIsLatestTrue(String consentType);
}
