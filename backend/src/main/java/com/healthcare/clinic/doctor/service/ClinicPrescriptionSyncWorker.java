package com.healthcare.clinic.doctor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.doctor.dto.OutboxPrescriptionPayload;
import com.healthcare.clinic.doctor.entity.ClinicOutboxEvent;
import com.healthcare.clinic.doctor.repository.ClinicOutboxEventRepository;
import com.healthcare.clinic.integration.PharmacyIntegrationClient;
import com.healthcare.clinic.integration.dto.PrescriptionIntegrationItemDTO;
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
    private final PharmacyIntegrationClient pharmacyIntegrationClient;
    private final ObjectMapper objectMapper;

    private static final int MAX_RETRIES = 5;

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        List<ClinicOutboxEvent> pendingEvents = clinicOutboxEventRepository.findByStatus("PENDING");
        for (ClinicOutboxEvent event : pendingEvents) {
            try {
                if (event.getRetryCount() > 0 && event.getProcessedAt() != null) {
                    ZonedDateTime nextRetry = event.getProcessedAt().plusSeconds(event.getRetryCount() * 30L);
                    if (ZonedDateTime.now().isBefore(nextRetry)) {
                        continue;
                    }
                }

                if ("PRESCRIPTION".equals(event.getAggregateType())) {
                    if ("PRESCRIPTION_CREATED".equals(event.getEventType())) {
                        OutboxPrescriptionPayload payload = objectMapper.readValue(event.getPayload(), OutboxPrescriptionPayload.class);
                        List<PrescriptionIntegrationItemDTO> pharmItems = mapItems(payload.getItems());
                        pharmacyIntegrationClient.syncNewPrescription(
                                payload.getPatientName(), payload.getDoctorName(), payload.getClinicalPrescriptionId(), pharmItems);
                    } else if ("PRESCRIPTION_SENT".equals(event.getEventType())) {
                        OutboxPrescriptionPayload payload = objectMapper.readValue(event.getPayload(), OutboxPrescriptionPayload.class);
                        List<PrescriptionIntegrationItemDTO> pharmItems = mapItems(payload.getItems());
                        pharmacyIntegrationClient.syncSendPrescription(
                                payload.getPatientName(), payload.getDoctorName(), payload.getClinicalPrescriptionId(), payload.getPharmacyUserId(), pharmItems);
                    } else if ("PRESCRIPTION_VOIDED".equals(event.getEventType())) {
                        pharmacyIntegrationClient.syncVoidPrescription(Long.valueOf(event.getAggregateId()));
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
                event.setProcessedAt(ZonedDateTime.now());
                
                if (attempts >= MAX_RETRIES) {
                    event.setStatus("FAILED");
                    log.error("OUTBOX SYNC FAILED PERMANENTLY: EventID={}, Type={}, AggregateID={}, Error={}", 
                            event.getId(), event.getEventType(), event.getAggregateId(), e.getMessage(), e);
                } else {
                    event.setStatus("PENDING");
                    log.warn("Outbox sync failed (Attempt {}/{}). Will retry later. EventID={}, Error={}", 
                            attempts, MAX_RETRIES, event.getId(), e.getMessage());
                }
                clinicOutboxEventRepository.save(event);
            }
        }
    }

    private List<PrescriptionIntegrationItemDTO> mapItems(List<OutboxPrescriptionPayload.OutboxPrescriptionItem> items) {
        return items.stream().map(item -> PrescriptionIntegrationItemDTO.builder()
                .medicationName(item.getMedicationName())
                .type(item.getType())
                .dosage(item.getDosage())
                .frequency(item.getFrequency())
                .duration(item.getDuration())
                .instructions(item.getInstructions())
                .strength(item.getStrength())
                .timing(item.getTiming())
                .medicineId(item.getMedicineId())
                .prescribedQuantity(item.getPrescribedQuantity())
                .dispensedQuantity(item.getDispensedQuantity())
                .remainingQuantity(item.getRemainingQuantity())
                .build()).collect(Collectors.toList());
    }
}
