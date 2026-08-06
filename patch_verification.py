import sys

filepath = 'backend/src/main/java/com/healthcare/clinic/pharmacy/service/PrescriptionVerificationService.java'
with open(filepath, 'r') as f:
    content = f.read()

imports = """
import com.healthcare.clinic.pharmacy.entity.PharmacyOutboxEvent;
import com.healthcare.clinic.pharmacy.repository.PharmacyOutboxEventRepository;
import com.healthcare.clinic.pharmacy.dto.OutboxStatusUpdatePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
"""
content = content.replace('import org.springframework.stereotype.Service;', imports + 'import org.springframework.stereotype.Service;')

fields = """
    private final PrescriptionRepository prescriptionRepository;
    private final PharmacyOutboxEventRepository pharmacyOutboxEventRepository;
    private final ObjectMapper objectMapper;

    public PrescriptionVerificationService(
            PrescriptionRepository prescriptionRepository,
            PharmacyOutboxEventRepository pharmacyOutboxEventRepository,
            ObjectMapper objectMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.pharmacyOutboxEventRepository = pharmacyOutboxEventRepository;
        this.objectMapper = objectMapper;
    }

    private void saveOutboxEvent(Long id, String status, String username, LocalDateTime dispensedAt) {
        try {
            OutboxStatusUpdatePayload payload = OutboxStatusUpdatePayload.builder()
                .clinicalPrescriptionId(id)
                .status(status)
                .pharmacistUsername(username)
                .dispensedAt(dispensedAt)
                .build();
            PharmacyOutboxEvent event = PharmacyOutboxEvent.builder()
                .aggregateType("PHARMACY_PRESCRIPTION")
                .aggregateId(id != null ? id.toString() : "0")
                .eventType("STATUS_UPDATE")
                .payload(objectMapper.writeValueAsString(payload))
                .status("PENDING")
                .build();
            pharmacyOutboxEventRepository.save(event);
        } catch(Exception e) {
            throw new RuntimeException("Failed to serialize outbox event", e);
        }
    }
"""

# Replace the fields and constructor
target_constructor = """
    private final PrescriptionRepository prescriptionRepository;
    private final com.healthcare.clinic.doctor.service.ClinicPrescriptionSyncService clinicPrescriptionSyncService;

    public PrescriptionVerificationService(
            PrescriptionRepository prescriptionRepository,
            com.healthcare.clinic.doctor.service.ClinicPrescriptionSyncService clinicPrescriptionSyncService) {
        this.prescriptionRepository = prescriptionRepository;
        this.clinicPrescriptionSyncService = clinicPrescriptionSyncService;
    }
"""
content = content.replace(target_constructor, fields)

content = content.replace('clinicPrescriptionSyncService.syncClinicalStatus(saved.getClinicalPrescriptionId(), "VERIFIED", pharmacistUsername, null);', 'saveOutboxEvent(saved.getClinicalPrescriptionId(), "VERIFIED", pharmacistUsername, null);')
content = content.replace('clinicPrescriptionSyncService.syncClinicalStatus(saved.getClinicalPrescriptionId(), "CANCELLED", pharmacistUsername, null);', 'saveOutboxEvent(saved.getClinicalPrescriptionId(), "CANCELLED", pharmacistUsername, null);')
content = content.replace('clinicPrescriptionSyncService.syncClinicalStatus(saved.getClinicalPrescriptionId(), "DISPENSED", pharmacistUsername, saved.getDispensedAt());', 'saveOutboxEvent(saved.getClinicalPrescriptionId(), "DISPENSED", pharmacistUsername, saved.getDispensedAt());')

with open(filepath, 'w') as f:
    f.write(content)
