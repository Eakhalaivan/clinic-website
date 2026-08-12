package com.healthcare.clinic.superadmin.service;

import com.healthcare.clinic.superadmin.entity.FeatureFlag;
import com.healthcare.clinic.superadmin.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureFlagService {
    private final FeatureFlagRepository repository;

    public List<FeatureFlag> getAllFlags() {
        return repository.findAll();
    }

    public FeatureFlag createOrUpdateFlag(FeatureFlag flag) {
        return repository.save(flag);
    }
    
    public boolean isFeatureEnabled(String flagKey) {
        return repository.findByFlagKey(flagKey)
                .map(FeatureFlag::isEnabled)
                .orElse(false);
    }
}
