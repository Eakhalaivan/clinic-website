package com.healthcare.clinic.tenant.repository;

import com.healthcare.clinic.tenant.entity.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
}
