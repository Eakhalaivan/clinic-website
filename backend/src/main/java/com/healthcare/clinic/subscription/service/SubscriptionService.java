package com.healthcare.clinic.subscription.service;

import com.healthcare.clinic.subscription.entity.Subscription;
import com.healthcare.clinic.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public List<Subscription> findAll() {
        return repository.findAll();
    }

    public Optional<Subscription> findById(Long id) {
        return repository.findById(id);
    }

    public Subscription save(Subscription entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
