package com.healthcare.clinic.doctor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.doctor.entity.ClinicOutboxEvent;
import com.healthcare.clinic.doctor.repository.ClinicOutboxEventRepository;
import com.healthcare.clinic.pharmacy.service.PharmacyPrescriptionSyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClinicPrescriptionSyncWorkerTest {

    @Mock
    private ClinicOutboxEventRepository clinicOutboxEventRepository;

    @Mock
    private PharmacyPrescriptionSyncService pharmacyPrescriptionSyncService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ClinicPrescriptionSyncWorker worker;

    private ClinicOutboxEvent event;

    @BeforeEach
    void setUp() {
        event = new ClinicOutboxEvent();
        event.setId(1L);
        event.setAggregateType("PRESCRIPTION");
        event.setEventType("PRESCRIPTION_VOIDED"); // simple event type to test failure
        event.setAggregateId("123");
        event.setPayload("{}");
        event.setStatus("PENDING");
        event.setRetryCount(0);
    }

    @Test
    void testProcessOutbox_Failure_RetryIncremented() throws Exception {
        when(clinicOutboxEventRepository.findByStatus("PENDING")).thenReturn(Collections.singletonList(event));
        doThrow(new RuntimeException("Simulated failure")).when(pharmacyPrescriptionSyncService).syncVoidPrescription(anyLong());

        worker.processOutbox();

        assertEquals(1, event.getRetryCount());
        assertEquals("PENDING", event.getStatus());
        assertEquals("Simulated failure", event.getLastError());
        verify(clinicOutboxEventRepository).save(event);
    }

    @Test
    void testProcessOutbox_Failure_MaxRetriesReached() throws Exception {
        event.setRetryCount(4);
        when(clinicOutboxEventRepository.findByStatus("PENDING")).thenReturn(Collections.singletonList(event));
        doThrow(new RuntimeException("Simulated failure")).when(pharmacyPrescriptionSyncService).syncVoidPrescription(anyLong());

        worker.processOutbox();

        assertEquals(5, event.getRetryCount());
        assertEquals("FAILED", event.getStatus()); // MAX_RETRIES = 5
        assertEquals("Simulated failure", event.getLastError());
        verify(clinicOutboxEventRepository).save(event);
    }

    @Test
    void testProcessOutbox_BackoffSkipsRetry() throws Exception {
        event.setRetryCount(2);
        event.setProcessedAt(ZonedDateTime.now().minusSeconds(10)); // Not enough time passed (needs 60s)
        when(clinicOutboxEventRepository.findByStatus("PENDING")).thenReturn(Collections.singletonList(event));

        worker.processOutbox();

        // Should be skipped, so syncVoidPrescription should not be called
        verify(pharmacyPrescriptionSyncService, never()).syncVoidPrescription(anyLong());
        // Since it's skipped, save shouldn't be called either
        verify(clinicOutboxEventRepository, never()).save(any());
    }
}
