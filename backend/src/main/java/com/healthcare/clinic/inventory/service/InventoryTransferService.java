package com.healthcare.clinic.inventory.service;

import com.healthcare.clinic.inventory.entity.InventoryTransfer;
import com.healthcare.clinic.inventory.repository.InventoryTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryTransferService {

    private final InventoryTransferRepository repository;

    public List<InventoryTransfer> findAll() {
        return repository.findAll();
    }

    public Optional<InventoryTransfer> findById(Long id) {
        return repository.findById(id);
    }

    public InventoryTransfer save(InventoryTransfer entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
