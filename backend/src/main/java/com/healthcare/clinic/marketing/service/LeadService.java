package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.Lead;
import com.healthcare.clinic.marketing.entity.LeadActivity;
import com.healthcare.clinic.marketing.repository.LeadActivityRepository;
import com.healthcare.clinic.marketing.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.HexFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final LeadActivityRepository activityRepository;

    private static final String[] VALID_STATUSES = {
            "NEW", "CONTACTED", "QUALIFIED", "APPOINTMENT_BOOKED",
            "CONVERTED", "NURTURING", "LOST", "ARCHIVED"
    };

    /**
     * Creates a lead with deduplication.
     * Returns existing lead if duplicate key matches (same normalized phone+email).
     */
    @Transactional
    public Lead createOrGetExistingLead(Lead lead) {
        String dedupKey = buildDeduplicationKey(lead.getPhone(), lead.getEmail());
        lead.setDeduplicationKey(dedupKey);

        Optional<Lead> existing = leadRepository.findByDeduplicationKey(dedupKey);
        if (existing.isPresent()) {
            // Return existing lead instead of creating duplicate
            return existing.get();
        }
        return leadRepository.save(lead);
    }

    @Transactional(readOnly = true)
    public Page<Lead> getPipeline(Long branchId, String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return leadRepository.findByBranchIdAndStatus(branchId, status, pageable);
        }
        return leadRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Lead getById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));
    }

    /**
     * Transitions lead status with audit record.
     * Validates allowed transitions to prevent invalid state changes.
     */
    @Transactional
    public Lead transitionStatus(Long leadId, String newStatus, Long performedBy, String notes) {
        Lead lead = getById(leadId);
        String oldStatus = lead.getStatus();
        validateTransition(oldStatus, newStatus);

        lead.setStatus(newStatus);
        lead.setUpdatedAt(ZonedDateTime.now());
        leadRepository.save(lead);

        // Record status change as an activity
        LeadActivity activity = LeadActivity.builder()
                .leadId(leadId)
                .activityType("STATUS_CHANGE")
                .notes("Status changed from " + oldStatus + " to " + newStatus
                        + (notes != null ? ". " + notes : ""))
                .performedBy(performedBy)
                .completedAt(ZonedDateTime.now())
                .build();
        activityRepository.save(activity);

        return lead;
    }

    @Transactional
    public LeadActivity addActivity(Long leadId, LeadActivity activity) {
        getById(leadId); // existence check
        activity.setLeadId(leadId);
        return activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public java.util.List<LeadActivity> getActivities(Long leadId) {
        return activityRepository.findByLeadIdOrderByCreatedAtDesc(leadId);
    }

    /**
     * Converts a lead to a patient record.
     * Sets the convertedPatientId and transitions status to CONVERTED.
     * Full patient registration must happen via the identity/registration service;
     * this method records the link and audit trail only.
     */
    @Transactional
    public Lead convertToPatient(Long leadId, Long patientId, Long performedBy) {
        Lead lead = getById(leadId);
        if ("CONVERTED".equals(lead.getStatus())) {
            throw new IllegalStateException("Lead is already converted");
        }
        lead.setConvertedPatientId(patientId);
        lead.setStatus("CONVERTED");
        lead.setUpdatedAt(ZonedDateTime.now());

        LeadActivity activity = LeadActivity.builder()
                .leadId(leadId)
                .activityType("CONVERTED_TO_PATIENT")
                .notes("Lead converted to patient ID: " + patientId)
                .performedBy(performedBy)
                .completedAt(ZonedDateTime.now())
                .build();
        activityRepository.save(activity);

        return leadRepository.save(lead);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private String buildDeduplicationKey(String phone, String email) {
        String normalized = normalizePhone(phone) + "|" + normalizeEmail(email);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(normalized.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private String normalizePhone(String phone) {
        return phone == null ? "" : phone.replaceAll("[^0-9]", "");
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private void validateTransition(String from, String to) {
        // Simple allow-all for now; can be extended to a state machine
        boolean validTo = false;
        for (String s : VALID_STATUSES) {
            if (s.equals(to)) { validTo = true; break; }
        }
        if (!validTo) {
            throw new IllegalArgumentException("Invalid lead status: " + to);
        }
    }
}
