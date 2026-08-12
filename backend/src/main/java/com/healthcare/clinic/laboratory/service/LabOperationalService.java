package com.healthcare.clinic.laboratory.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.laboratory.entity.LabInventoryItem;
import com.healthcare.clinic.laboratory.entity.LabQualityControl;
import com.healthcare.clinic.laboratory.entity.LabTestCatalog;
import com.healthcare.clinic.laboratory.repository.LabInventoryItemRepository;
import com.healthcare.clinic.laboratory.repository.LabQualityControlRepository;
import com.healthcare.clinic.laboratory.repository.LabTestCatalogRepository;
import com.healthcare.clinic.laboratory.repository.LabTestRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LabOperationalService {

    private final LabInventoryItemRepository inventoryRepository;
    private final LabQualityControlRepository qcRepository;
    private final LabTestCatalogRepository catalogRepository;
    private final LabTestRequestRepository requestRepository;

    @Transactional
    public LabInventoryItem deductInventory(String sku, int quantity) {
        LabInventoryItem item = inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Inventory item not found for sku: " + sku));

        if (item.getQuantity() < quantity) {
            throw new IllegalStateException("Insufficient inventory for sku: " + sku);
        }

        item.setQuantity(item.getQuantity() - quantity);
        return inventoryRepository.save(item);
    }

    @Transactional
    public LabQualityControl recordQualityControl(Long testCatalogId, String status, String notes, User user) {
        LabTestCatalog catalog = catalogRepository.findById(testCatalogId)
                .orElseThrow(() -> new IllegalArgumentException("Test catalog not found"));

        LabQualityControl qc = LabQualityControl.builder()
                .testCatalog(catalog)
                .status(status)
                .notes(notes)
                .performedBy(user)
                .branch(catalog.getBranch())
                .build();

        return qcRepository.save(qc);
    }

    @Transactional(readOnly = true)
    public void validateQcPassed(Long testCatalogId) {
        List<LabQualityControl> qcs = qcRepository.findByTestCatalogIdOrderByPerformedAtDesc(testCatalogId);
        if (!qcs.isEmpty()) {
            LabQualityControl latestQc = qcs.get(0);
            if ("FAILED".equalsIgnoreCase(latestQc.getStatus())) {
                throw new IllegalStateException("Test cannot be performed. Quality Control FAILED for this test.");
            }
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStats(Long branchId) {
        long pendingRequests = requestRepository.count(); // In reality, filter by status & branch
        long lowStockItems = inventoryRepository.findAll().stream()
                .filter(i -> i.getQuantity() <= i.getMinimumThreshold())
                .count();

        return Map.of(
                "pendingRequests", pendingRequests,
                "lowStockItems", lowStockItems
        );
    }
}
