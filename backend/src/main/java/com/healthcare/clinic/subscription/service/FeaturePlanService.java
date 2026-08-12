package com.healthcare.clinic.subscription.service;

import com.healthcare.clinic.subscription.entity.FeaturePlan;
import com.healthcare.clinic.subscription.repository.FeaturePlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeaturePlanService {

    private final FeaturePlanRepository repository;

    public List<FeaturePlan> findAll() {
        return repository.findAll();
    }

    public Optional<FeaturePlan> findById(Long id) {
        return repository.findById(id);
    }

    public FeaturePlan save(FeaturePlan entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
