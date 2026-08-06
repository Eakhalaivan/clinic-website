import sys

filepath = 'backend/src/main/java/com/healthcare/clinic/doctor/service/PrescriptionService.java'
with open(filepath, 'r') as f:
    content = f.read()

target = "pharmacyPrescriptionSyncService.syncVoidPrescription(id);"
replacement = """
        try {
            ClinicOutboxEvent event = ClinicOutboxEvent.builder()
                    .aggregateType("PRESCRIPTION")
                    .aggregateId(id.toString())
                    .eventType("PRESCRIPTION_VOIDED")
                    .payload("{}")
                    .status("PENDING")
                    .build();
            outboxEventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize outbox event", e);
        }
"""
content = content.replace(target, replacement)
with open(filepath, 'w') as f:
    f.write(content)
