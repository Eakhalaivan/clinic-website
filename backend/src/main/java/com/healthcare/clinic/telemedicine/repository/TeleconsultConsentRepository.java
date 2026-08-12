package com.healthcare.clinic.telemedicine.repository;
import com.healthcare.clinic.telemedicine.entity.TeleconsultConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TeleconsultConsentRepository extends JpaRepository<TeleconsultConsent, Long> {}
