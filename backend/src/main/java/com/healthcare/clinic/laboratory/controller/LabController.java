package com.healthcare.clinic.laboratory.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.laboratory.entity.LabResult;
import com.healthcare.clinic.laboratory.entity.LabTestCatalog;
import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.laboratory.repository.LabResultRepository;
import com.healthcare.clinic.laboratory.repository.LabTestCatalogRepository;
import com.healthcare.clinic.laboratory.repository.LabTestRequestRepository;
import com.healthcare.clinic.notification.event.LabResultReleasedEvent;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lab")
@RequiredArgsConstructor
public class LabController {

    private final LabTestCatalogRepository catalogRepository;
    private final LabTestRequestRepository requestRepository;
    private final LabResultRepository resultRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;

    // ─── Patient: own lab reports ─────────────────────────────────────────────

    /**
     * GET /api/patient/lab-reports
     * Returns all lab test requests for the currently logged-in patient.
     */
    @GetMapping("/patient/lab-reports")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<LabTestRequest>> getMyLabReports() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        PatientProfile profile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Patient profile not found for user " + userId));
        return ResponseEntity.ok(
                requestRepository.findByPatientIdOrderByRequestedAtDesc(profile.getId()));
    }

    // ─── Doctor: lab requests I ordered ────────────────────────────────────────

    /**
     * GET /api/lab/doctor/my-requests
     * Returns all lab test requests ordered by the currently logged-in doctor.
     */
    @GetMapping("/doctor/my-requests")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<LabTestRequest>> getMyDoctorLabRequests() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return ResponseEntity.ok(
                requestRepository.findByDoctorUserIdOrderByRequestedAtDesc(userId));
    }

    // ─── Catalog ──────────────────────────────────────────────────────────────

    @GetMapping("/catalog")
    public ResponseEntity<List<LabTestCatalog>> getCatalog() {
        return ResponseEntity.ok(catalogRepository.findByIsActiveTrue());
    }

    @PostMapping("/requests")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<LabTestRequest> createRequest(@RequestBody LabTestRequest request) {
        request.setStatus("REQUESTED");
        request.setRequestedAt(ZonedDateTime.now());
        return ResponseEntity.ok(requestRepository.save(request));
    }

    @GetMapping("/requests/status/{status}")
    @PreAuthorize("hasRole('LAB_TECH') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<LabTestRequest>> getRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(requestRepository.findByStatus(status));
    }

    @PutMapping("/requests/{requestId}/status")
    @PreAuthorize("hasRole('LAB_TECH') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<LabTestRequest> updateRequestStatus(@PathVariable Long requestId, @RequestParam String status) {
        LabTestRequest request = requestRepository.findById(requestId).orElseThrow();
        request.setStatus(status);
        if ("SAMPLE_COLLECTED".equals(status)) {
            request.setSampleCollectedAt(ZonedDateTime.now());
        }
        LabTestRequest saved = requestRepository.save(request);

        // Publish notification when result is released
        if ("RELEASED".equals(status) && saved.getPatient() != null) {
            Long patientUserId = saved.getPatient().getUserId();
            String patientName = userRepository.findById(patientUserId)
                    .map(u -> u.getFirstName() + " " + u.getLastName())
                    .orElse("Patient");
            String patientEmail = userRepository.findById(patientUserId)
                    .map(u -> u.getEmail()).orElse(null);
            eventPublisher.publishEvent(LabResultReleasedEvent.builder()
                    .requestId(requestId)
                    .patientId(patientUserId)
                    .patientName(patientName)
                    .patientEmail(patientEmail)
                    .testName(saved.getTestCatalog() != null ? saved.getTestCatalog().getTestName() : "Unknown")
                    .build());
        }
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/requests/{requestId}/result")
    @PreAuthorize("hasRole('LAB_TECH') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<LabResult> addResult(@PathVariable Long requestId, @RequestBody LabResult result, @AuthenticationPrincipal User labTech) {
        LabTestRequest request = requestRepository.findById(requestId).orElseThrow();
        result.setRequest(request);
        result.setLabTech(labTech);
        result.setEnteredAt(ZonedDateTime.now());
        
        request.setStatus("RESULT_ENTERED");
        requestRepository.save(request);
        
        return ResponseEntity.ok(resultRepository.save(result));
    }
}
