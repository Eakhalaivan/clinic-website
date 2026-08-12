package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.ReferralProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralProgramRepository extends JpaRepository<ReferralProgram, Long> {
    List<ReferralProgram> findByStatus(String status);
}
