package com.healthcare.clinic.pharmacy.service;

import com.healthcare.clinic.pharmacy.entity.WardReplacementReturn;
import com.healthcare.clinic.pharmacy.repository.WardReplacementReturnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WardReplacementReturnService {

    private final WardReplacementReturnRepository repository;

    public WardReplacementReturnService(WardReplacementReturnRepository repository) {
        this.repository = repository;
    }

    public List<WardReplacementReturn> getPendingReturns() {
        return repository.findByStatusOrderByReturnDateDesc("PENDING");
    }

    public List<WardReplacementReturn> getAllReturns() {
        return repository.findAllByOrderByReturnDateDesc();
    }

    @Transactional
    public void approve(Long id) {
        WardReplacementReturn request = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Replacement return not found: " + id));
        request.setStatus("COMPLETED");
        repository.save(request);
    }

    @Transactional
    public void reject(Long id) {
        WardReplacementReturn request = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Replacement return not found: " + id));
        request.setStatus("REJECTED");
        repository.save(request);
    }
}
