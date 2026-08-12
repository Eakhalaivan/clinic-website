package com.healthcare.clinic.subscription.repository;

import com.healthcare.clinic.subscription.entity.FeaturePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturePlanRepository extends JpaRepository<FeaturePlan, Long> {
}
