package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.radiology.entity.RadiologyInventoryItem;
import com.healthcare.clinic.radiology.repository.RadiologyInventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RadiologyOperationalService {

    private final RadiologyInventoryItemRepository inventoryRepository;

    @Transactional
    public RadiologyInventoryItem deductInventory(String sku, Long branchId, int quantity) {
        RadiologyInventoryItem item = inventoryRepository.findBySkuAndBranchId(sku, branchId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory item not found for sku: " + sku));

        if (item.getQuantity() < quantity) {
            throw new IllegalStateException("Insufficient inventory for sku: " + sku + ". Available: " + item.getQuantity() + ", Requested: " + quantity);
        }

        item.setQuantity(item.getQuantity() - quantity);
        return inventoryRepository.save(item);
    }

    public List<RadiologyInventoryItem> getLowStockItems(Long branchId) {
        return inventoryRepository.findByBranchId(branchId).stream()
                .filter(item -> item.getQuantity() <= item.getMinimumThreshold())
                .toList();
    }
}
