import sys

filepath = 'backend/src/main/java/com/healthcare/clinic/doctor/service/PrescriptionService.java'
with open(filepath, 'r') as f:
    content = f.read()

# Remove the field
content = content.replace("private final com.healthcare.clinic.pharmacy.service.PharmacyPrescriptionSyncService pharmacyPrescriptionSyncService;\n", "")

# Replace syncSendPrescription
target_send = """
        List<com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionItem> pharmItems = prescription.getItems().stream().map(item -> 
            PharmacyPrescriptionItem.builder()
                .medicationName(item.getMedicationName())
                .type(item.getType())
                .dosage(item.getDosage())
                .frequency(item.getFrequency())
                .duration(item.getDuration())
                .instructions(item.getInstructions())
                .strength(item.getStrength())
                .timing(item.getTiming())
                .build()
        ).collect(Collectors.toList());

        pharmacyPrescriptionSyncService.syncSendPrescription(patientName, doctorName, saved.getId(), pharmacyUserId, pharmItems);
"""

replacement_send = """
        List<OutboxPrescriptionPayload.OutboxPrescriptionItem> outboxItems = prescription.getItems().stream().map(item -> 
            OutboxPrescriptionPayload.OutboxPrescriptionItem.builder()
                .medicationName(item.getMedicationName())
                .type(item.getType())
                .dosage(item.getDosage())
                .frequency(item.getFrequency())
                .duration(item.getDuration())
                .instructions(item.getInstructions())
                .strength(item.getStrength())
                .timing(item.getTiming())
                .build()
        ).collect(Collectors.toList());

        OutboxPrescriptionPayload payload = OutboxPrescriptionPayload.builder()
                .patientName(patientName)
                .doctorName(doctorName)
                .clinicalPrescriptionId(saved.getId())
                .pharmacyUserId(pharmacyUserId)
                .items(outboxItems)
                .build();
        
        try {
            ClinicOutboxEvent event = ClinicOutboxEvent.builder()
                    .aggregateType("PRESCRIPTION")
                    .aggregateId(saved.getId().toString())
                    .eventType("PRESCRIPTION_SENT")
                    .payload(objectMapper.writeValueAsString(payload))
                    .status("PENDING")
                    .build();
            outboxEventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize outbox event", e);
        }
"""
content = content.replace(target_send, replacement_send)

with open(filepath, 'w') as f:
    f.write(content)
