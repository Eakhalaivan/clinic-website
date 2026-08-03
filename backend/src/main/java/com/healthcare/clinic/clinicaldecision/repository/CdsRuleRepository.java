package com.healthcare.clinic.clinicaldecision.repository;

import com.healthcare.clinic.clinicaldecision.entity.CdsRule;
import com.healthcare.clinic.clinicaldecision.entity.TriggerEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CdsRuleRepository extends JpaRepository<CdsRule, Long> {
    List<CdsRule> findByIsActiveTrue();
    List<CdsRule> findByTriggerEventAndIsActiveTrue(TriggerEvent triggerEvent);
}
