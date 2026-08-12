package com.healthcare.clinic.tenant.service;

import com.healthcare.clinic.tenant.entity.Tenant;
import com.healthcare.clinic.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository repository;

    public List<Tenant> findAll() {
        return repository.findAll();
    }

    public Optional<Tenant> findById(Long id) {
        return repository.findById(id);
    }

    public Tenant save(Tenant entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
