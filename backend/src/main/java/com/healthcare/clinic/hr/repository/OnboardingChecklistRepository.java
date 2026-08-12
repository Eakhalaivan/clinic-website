package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.OnboardingChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnboardingChecklistRepository extends JpaRepository<OnboardingChecklist, Long> {
    Optional<OnboardingChecklist> findByEmployeeId(Long employeeId);
}
