package com.healthcare.clinic.patient.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.*;
import com.healthcare.clinic.patient.service.PatientSettingsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/patient/settings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PATIENT')")
public class PatientSettingsController {

    private final PatientSettingsService patientSettingsService;

    // --- Dependents ---
    @GetMapping("/dependents")
    public ResponseEntity<List<DependentProfile>> getDependents(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(patientSettingsService.getDependents(user));
    }

    @PostMapping("/dependents")
    public ResponseEntity<DependentProfile> addDependent(@AuthenticationPrincipal User user, @RequestBody DependentProfile dependent) {
        return ResponseEntity.ok(patientSettingsService.addDependent(user, dependent));
    }

    @DeleteMapping("/dependents/{id}")
    public ResponseEntity<Void> removeDependent(@AuthenticationPrincipal User user, @PathVariable Long id) {
        patientSettingsService.removeDependent(user, id);
        return ResponseEntity.noContent().build();
    }

    // --- Emergency Contacts ---
    @GetMapping("/emergency-contacts")
    public ResponseEntity<List<EmergencyContact>> getEmergencyContacts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(patientSettingsService.getEmergencyContacts(user));
    }

    @PostMapping("/emergency-contacts")
    public ResponseEntity<EmergencyContact> addEmergencyContact(@AuthenticationPrincipal User user, @RequestBody EmergencyContact contact) {
        return ResponseEntity.ok(patientSettingsService.addEmergencyContact(user, contact));
    }

    @DeleteMapping("/emergency-contacts/{id}")
    public ResponseEntity<Void> removeEmergencyContact(@AuthenticationPrincipal User user, @PathVariable Long id) {
        patientSettingsService.removeEmergencyContact(user, id);
        return ResponseEntity.noContent().build();
    }

    // --- Notification Preferences ---
    @GetMapping("/notifications")
    public ResponseEntity<List<PatientNotificationPreference>> getNotificationPreferences(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(patientSettingsService.getNotificationPreferences(user));
    }

    @PutMapping("/notifications/{category}")
    public ResponseEntity<PatientNotificationPreference> updateNotificationPreference(
            @AuthenticationPrincipal User user,
            @PathVariable String category,
            @RequestBody PatientNotificationPreference pref) {
        return ResponseEntity.ok(patientSettingsService.updateNotificationPreference(user, category, pref));
    }

    // --- Consents ---
    @GetMapping("/consents/versions")
    public ResponseEntity<List<ConsentVersion>> getLatestConsentVersions() {
        return ResponseEntity.ok(patientSettingsService.getLatestConsentVersions());
    }

    @GetMapping("/consents")
    public ResponseEntity<List<PatientConsent>> getPatientConsents(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(patientSettingsService.getPatientConsents(user));
    }

    @PostMapping("/consents/{consentType}")
    public ResponseEntity<PatientConsent> grantConsent(
            @AuthenticationPrincipal User user,
            @PathVariable String consentType,
            HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        return ResponseEntity.ok(patientSettingsService.grantConsent(user, consentType, ipAddress, userAgent));
    }

    @DeleteMapping("/consents/{consentType}")
    public ResponseEntity<Void> revokeConsent(
            @AuthenticationPrincipal User user,
            @PathVariable String consentType) {
        patientSettingsService.revokeConsent(user, consentType);
        return ResponseEntity.noContent().build();
    }
}
