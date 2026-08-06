package com.healthcare.clinic.doctor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.doctor.dto.OutboxPrescriptionPayload;
import com.healthcare.clinic.doctor.entity.ClinicOutboxEvent;
import com.healthcare.clinic.doctor.repository.ClinicOutboxEventRepository;
import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionItem;
import com.healthcare.clinic.pharmacy.service.PharmacyPrescriptionSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ClinicPrescriptionSyncWorker {

    private final ClinicOutboxEventRepository clinicOutboxEventRepository;
    private final PharmacyPrescriptionSyncService pharmacyPrescriptionSyncService;
    private final ObjectMapper objectMapper;

    private static final int MAX_RETRIES = 5;

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        List<ClinicOutboxEvent> pendingEvents = clinicOutboxEventRepository.findByStatus("PENDING");
        for (ClinicOutboxEvent event : pendingEvents) {
            try {
                // Backoff logic: lastAttemptAt + (retryCount * 30s)
                if (event.getRetryCount() > 0 && event.getProcessedAt() != null) {
                    ZonedDateTime nextRetry = event.getProcessedAt().plusSeconds(event.getRetryCount() * 30L);
                    if (ZonedDateTime.now().isBefore(nextRetry)) {
                        continue; // Skip this event for now
                    }
                }

                if ("PRESCRIPTION".equals(event.getAggregateType())) {
                    if ("PRESCRIPTION_CREATED".equals(event.getEventType())) {
                        OutboxPrescriptionPayload payload = objectMapper.readValue(event.getPayload(), OutboxPrescriptionPayload.class);
                        List<PharmacyPrescriptionItem> pharmItems = mapItems(payload.getItems());
                        pharmacyPrescriptionSyncService.syncNewPrescription(
                                payload.getPatientName(), payload.getDoctorName(), payload.getClinicalPrescriptionId(), pharmItems);
                    } else if ("PRESCRIPTION_SENT".equals(event.getEventType())) {
                        OutboxPrescriptionPayload payload = objectMapper.readValue(event.getPayload(), OutboxPrescriptionPayload.class);
                        List<PharmacyPrescriptionItem> pharmItems = mapItems(payload.getItems());
                        pharmacyPrescriptionSyncService.syncSendPrescription(
                                payload.getPatientName(), payload.getDoctorName(), payload.getClinicalPrescriptionId(), payload.getPharmacyUserId(), pharmItems);
                    } else if ("PRESCRIPTION_VOIDED".equals(event.getEventType())) {
                        pharmacyPrescriptionSyncService.syncVoidPrescription(Long.valueOf(event.getAggregateId()));
                    }
                }
                
                event.setStatus("PROCESSED");
                event.setProcessedAt(ZonedDateTime.now());
                event.setLastError(null);
                clinicOutboxEventRepository.save(event);
            } catch (Exception e) {
                int attempts = (event.getRetryCount() != null ? event.getRetryCount() : 0) + 1;
                event.setRetryCount(attempts);
                event.setLastError(e.getMessage());
                event.setProcessedAt(ZonedDateTime.now()); // Record attempt time
                
                if (attempts >= MAX_RETRIES) {
                    event.setStatus("FAILED");
                    log.error("OUTBOX SYNC FAILED PERMANENTLY: EventID={}, Type={}, AggregateID={}, Error={}", 
                            event.getId(), event.getEventType(), event.getAggregateId(), e.getMessage(), e);
                    // In a full implementation, we might write to a sync_failures table here.
                } else {
                    event.setStatus("PENDING");
                    log.warn("Outbox sync failed (Attempt {}/{}). Will retry later. EventID={}, Error={}", 
                            attempts, MAX_RETRIES, event.getId(), e.getMessage());
                }
                clinicOutboxEventRepository.save(event);
            }
        }
    }

    private List<PharmacyPrescriptionItem> mapItems(List<OutboxPrescriptionPayload.OutboxPrescriptionItem> items) {
        return items.stream().map(item -> PharmacyPrescriptionItem.builder()
                .medicationName(item.getMedicationName())
                .type(item.getType())
                .dosage(item.getDosage())
                .frequency(item.getFrequency())
                .duration(item.getDuration())
                .instructions(item.getInstructions())
                .strength(item.getStrength())
                .timing(item.getTiming())
                .build()).collect(Collectors.toList());
    }
}
