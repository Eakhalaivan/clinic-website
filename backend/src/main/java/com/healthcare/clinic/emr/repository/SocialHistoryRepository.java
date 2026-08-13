package com.healthcare.clinic.emr.repository;

import com.healthcare.clinic.emr.entity.SocialHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SocialHistoryRepository extends JpaRepository<SocialHistory, Long> {
    List<SocialHistory> findByPatientId(Long patientId);
}

