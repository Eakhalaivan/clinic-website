package com.healthcare.clinic.engagement.repository;

import com.healthcare.clinic.engagement.entity.PreventiveCareRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreventiveCareRuleRepository extends JpaRepository<PreventiveCareRule, Long> {
}
