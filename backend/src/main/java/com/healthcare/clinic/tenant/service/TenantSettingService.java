package com.healthcare.clinic.tenant.service;

import com.healthcare.clinic.tenant.entity.TenantSetting;
import com.healthcare.clinic.tenant.repository.TenantSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantSettingService {

    private final TenantSettingRepository repository;

    public List<TenantSetting> findAll() {
        return repository.findAll();
    }

    public Optional<TenantSetting> findById(Long id) {
        return repository.findById(id);
    }

    public TenantSetting save(TenantSetting entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
