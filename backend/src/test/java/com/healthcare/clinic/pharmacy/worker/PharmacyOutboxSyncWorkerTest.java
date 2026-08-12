package com.healthcare.clinic.pharmacy.worker;

import com.healthcare.clinic.doctor.entity.Prescription;
import com.healthcare.clinic.doctor.repository.PrescriptionRepository;
import com.healthcare.clinic.pharmacy.entity.PharmacyOutboxEvent;
import com.healthcare.clinic.pharmacy.repository.PharmacyOutboxEventRepository;
import com.healthcare.clinic.pharmacy.service.PharmacyOutboxSyncWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PharmacyOutboxSyncWorkerTest {

    @Autowired
    private PharmacyOutboxSyncWorker worker;

    @Autowired
    private PharmacyOutboxEventRepository outboxEventRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Test
    public void testSyncStatusUpdatesClinicPrescription() {
        // Setup
        Prescription p = new Prescription();
        p.setPharmacyStatus("PENDING");
        p.setDoctorId(1L);
        p.setPatientId(1L);
        // Other fields might be required depending on constraints
        p = prescriptionRepository.save(p);

        PharmacyOutboxEvent event = new PharmacyOutboxEvent();
        event.setAggregateType("PHARMACY_PRESCRIPTION");
        event.setAggregateId(p.getId().toString());
        event.setEventType("STATUS_UPDATE");
        event.setStatus("PENDING");
        event.setPayload("{\"clinicalPrescriptionId\": " + p.getId() + ", \"status\": \"DISPENSED\", \"pharmacistUsername\": \"ph1\", \"dispensedAt\": \"2026-08-08T10:00:00Z\"}");
        event = outboxEventRepository.save(event);

        // Act
        worker.processOutbox();

        // Assert
        Optional<PharmacyOutboxEvent> updatedEvent = outboxEventRepository.findById(event.getId());
        assertTrue(updatedEvent.isPresent());
        assertEquals("PROCESSED", updatedEvent.get().getStatus());

        Optional<Prescription> updatedPrescription = prescriptionRepository.findById(p.getId());
        assertTrue(updatedPrescription.isPresent());
        assertEquals("DISPENSED", updatedPrescription.get().getPharmacyStatus());
    }
}
