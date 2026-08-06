package com.healthcare.clinic.pharmacy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.doctor.service.ClinicPrescriptionSyncService;
import com.healthcare.clinic.pharmacy.dto.OutboxStatusUpdatePayload;
import com.healthcare.clinic.pharmacy.entity.PharmacyOutboxEvent;
import com.healthcare.clinic.pharmacy.repository.PharmacyOutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class PharmacyOutboxSyncWorker {

    private final PharmacyOutboxEventRepository pharmacyOutboxEventRepository;
    private final ClinicPrescriptionSyncService clinicPrescriptionSyncService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        List<PharmacyOutboxEvent> pendingEvents = pharmacyOutboxEventRepository.findByStatus("PENDING");
        for (PharmacyOutboxEvent event : pendingEvents) {
            try {
                if ("PHARMACY_PRESCRIPTION".equals(event.getAggregateType()) && "STATUS_UPDATE".equals(event.getEventType())) {
                    OutboxStatusUpdatePayload payload = objectMapper.readValue(event.getPayload(), OutboxStatusUpdatePayload.class);
                    clinicPrescriptionSyncService.syncClinicalStatus(
                            payload.getClinicalPrescriptionId(),
                            payload.getStatus(),
                            payload.getPharmacistUsername(),
                            payload.getDispensedAt());
                }
                
                event.setStatus("PROCESSED");
                event.setProcessedAt(ZonedDateTime.now());
                pharmacyOutboxEventRepository.save(event);
            } catch (Exception e) {
                log.error("Failed to process pharmacy outbox event: " + event.getId(), e);
                event.setStatus("FAILED");
                pharmacyOutboxEventRepository.save(event);
            }
        }
    }
}
