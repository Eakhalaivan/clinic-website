package com.healthcare.clinic.superadmin.repository;
import com.healthcare.clinic.superadmin.entity.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    Optional<FeatureFlag> findByFlagKey(String flagKey);
}
