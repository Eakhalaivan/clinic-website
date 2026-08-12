package com.healthcare.clinic.engagement.repository;

import com.healthcare.clinic.engagement.entity.WellnessProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WellnessProgramRepository extends JpaRepository<WellnessProgram, Long> {
}
