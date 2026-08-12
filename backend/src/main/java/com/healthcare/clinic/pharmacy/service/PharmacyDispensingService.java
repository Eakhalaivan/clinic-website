package com.healthcare.clinic.pharmacy.service;

import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionRecord;
import com.healthcare.clinic.pharmacy.entity.PrescriptionDispensed;
import com.healthcare.clinic.pharmacy.entity.PrescriptionDispensedItem;
import com.healthcare.clinic.pharmacy.entity.StockBatch;
import com.healthcare.clinic.pharmacy.entity.InventoryMovement;
import com.healthcare.clinic.pharmacy.repository.PrescriptionRepository;
import com.healthcare.clinic.pharmacy.repository.PrescriptionDispensedRepository;
import com.healthcare.clinic.pharmacy.repository.StockBatchRepository;
import com.healthcare.clinic.pharmacy.repository.InventoryMovementRepository;
import com.healthcare.clinic.integration.ClinicIntegrationClient;
import com.healthcare.clinic.integration.dto.PharmacyInvoiceItemDTO;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.pharmacy.dto.DispenseRequest;
import com.healthcare.clinic.pharmacy.dto.DispenseItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PharmacyDispensingService {

    private final PrescriptionRepository prescriptionRecordRepository;
    private final PrescriptionDispensedRepository dispensedRepository;
    private final StockBatchRepository stockBatchRepository;
    private final InventoryMovementRepository movementRepository;
    private final ClinicIntegrationClient clinicIntegrationClient;

    @Transactional(transactionManager = "pharmacyTransactionManager")
    public PrescriptionDispensed dispensePrescription(DispenseRequest request, User pharmacist) {
        // 1. Fetch Prescription Record with pessimistic lock to prevent concurrent dispenses
        PharmacyPrescriptionRecord record = prescriptionRecordRepository.findByIdForUpdate(request.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        // 2. Idempotency Check — must happen INSIDE the lock so concurrent retries
        //    are safely short-circuited once the first write commits.
        if (request.getIdempotencyKey() != null) {
            java.util.Optional<PrescriptionDispensed> existing = dispensedRepository.findByIdempotencyKey(request.getIdempotencyKey());
            if (existing.isPresent()) {
                log.info("Idempotent retry for key: {}", request.getIdempotencyKey());
                return existing.get();
            }
        }

        if (!"VERIFIED".equals(record.getVerificationStatus())) {
            throw new IllegalStateException("Prescription must be verified before dispensing");
        }
        
        if ("DISPENSED".equals(record.getStatus())) {
            throw new IllegalStateException("Prescription is already dispensed");
        }


        String txRef = UUID.randomUUID().toString();

        PrescriptionDispensed dispensed = PrescriptionDispensed.builder()
                .prescriptionId(record.getId())
                .pharmacistId(pharmacist.getId())
                .dispensedAt(ZonedDateTime.now())
                .notes(request.getNotes())
                .idempotencyKey(request.getIdempotencyKey())
                .transactionReference(txRef)
                .partialDispense(request.isPartialDispense())
                .build();

        List<PrescriptionDispensedItem> dispensedItems = new ArrayList<>();
        List<PharmacyInvoiceItemDTO> invoiceItems = new ArrayList<>();

        for (DispenseItemRequest itemRequest : request.getItems()) {
            int remainingQuantity = itemRequest.getQuantity();

            // Fetch batches for medicine sorted by expiry ASC (FEFO) with PESSIMISTIC_WRITE lock
            List<StockBatch> batches = stockBatchRepository.findBatchesForDispensingWithLock(itemRequest.getMedicineId());

            for (StockBatch batch : batches) {
                if (remainingQuantity <= 0) break;

                int available = batch.getQuantityAvailable();
                int deduct = Math.min(available, remainingQuantity);

                batch.setQuantityAvailable(available - deduct);
                stockBatchRepository.save(batch);

                // Record Dispensed Item
                PrescriptionDispensedItem dispensedItem = PrescriptionDispensedItem.builder()
                        .dispensed(dispensed)
                        .medicine(batch.getMedicine())
                        .batchId(batch.getBatchId())
                        .quantityDispensed(deduct)
                        .priceCharged(batch.getMrp())
                        .expiryDate(batch.getExpiryDate())
                        .build();
                
                dispensedItems.add(dispensedItem);

                // Record Inventory Movement
                InventoryMovement movement = InventoryMovement.builder()
                        .medicineId(itemRequest.getMedicineId())
                        .batchId(batch.getBatchId())
                        .movementType("DISPENSE")
                        .quantity(-deduct)
                        .referenceId(txRef)
                        .referenceType("PRESCRIPTION_DISPENSE")
                        .userId(pharmacist.getId())
                        .build();
                movementRepository.save(movement);

                // Add to Invoice
                invoiceItems.add(PharmacyInvoiceItemDTO.builder()
                        .description(batch.getMedicineName() + " (Batch: " + batch.getBatchNumber() + ")")
                        .quantity(deduct)
                        .unitPrice(batch.getMrp())
                        .build());

                remainingQuantity -= deduct;
            }

            if (remainingQuantity > 0) {
                if (!request.isPartialDispense()) {
                    throw new IllegalStateException("Insufficient stock for medicine ID: " + itemRequest.getMedicineId() + ". Short by " + remainingQuantity);
                }
            }
        }

        dispensed.setItems(dispensedItems);

        // Save dispensed record
        PrescriptionDispensed savedDispensed = dispensedRepository.save(dispensed);
        
        // Update Prescription Record
        record.setStatus(request.isPartialDispense() ? "PARTIALLY_DISPENSED" : "DISPENSED");
        record.setDispensedAt(LocalDateTime.now());
        record.setDispensedBy(pharmacist.getUsername());
        prescriptionRecordRepository.save(record);

        if (!invoiceItems.isEmpty()) {
            try {
                clinicIntegrationClient.createPharmacyInvoice(
                        record.getClinicalPrescriptionId(),
                        invoiceItems,
                        "Pharmacy Dispense - Prescription #" + record.getId()
                );
            } catch (Exception e) {
                log.error("Failed to create invoice for dispense", e);
                throw new RuntimeException("Failed to generate invoice", e);
            }
        }

        return savedDispensed;
    }
}
