package com.healthcare.clinic.superadmin.service;

import com.healthcare.clinic.superadmin.entity.ApiKey;
import com.healthcare.clinic.superadmin.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IntegrationService {
    private final ApiKeyRepository repository;

    public List<ApiKey> getAllApiKeys() {
        return repository.findAll();
    }

    public ApiKey createApiKey(ApiKey key) {
        return repository.save(key);
    }
    
    public void revokeApiKey(Long id) {
        repository.findById(id).ifPresent(k -> {
            k.setRevoked(true);
            repository.save(k);
        });
    }
}
