package com.healthcare.clinic.tenant.service;

import com.healthcare.clinic.tenant.entity.FeatureFlag;
import com.healthcare.clinic.tenant.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeatureFlagService {

    private final FeatureFlagRepository repository;

    public List<FeatureFlag> findAll() {
        return repository.findAll();
    }

    public Optional<FeatureFlag> findById(Long id) {
        return repository.findById(id);
    }

    public FeatureFlag save(FeatureFlag entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
