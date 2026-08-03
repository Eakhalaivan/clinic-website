package com.healthcare.clinic.patient.controller;

import com.healthcare.clinic.patient.dto.PatientProfileRequest;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.security.SecurityUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientProfileRepository patientRepository;
    private final com.healthcare.clinic.patient.repository.VitalsRepository vitalsRepository;
    private final com.healthcare.clinic.identity.repository.UserRepository userRepository;

    @GetMapping("/profile/{userId}")
    @PreAuthorize("hasAuthority('ROLE_PATIENT') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PatientProfile> getPatientProfile(@PathVariable Long userId) {
        com.healthcare.clinic.security.SecurityUtils.assertOwnerOrAdmin(userId);
        return patientRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{patientId}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PatientProfile> getPatientById(@PathVariable Long patientId) {
        return patientRepository.findById(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<PatientProfile> createOrUpdateProfile(@jakarta.validation.Valid @RequestBody PatientProfileRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Optional<PatientProfile> existing = patientRepository.findByUserId(currentUserId);
        
        PatientProfile profile = existing.orElse(new PatientProfile());
        profile.setUserId(currentUserId);
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setBloodGroup(request.getBloodGroup());
        profile.setEmergencyContactName(request.getEmergencyContactName());
        profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        profile.setAddress(request.getAddress());
        profile.setMedicalHistorySummary(request.getMedicalHistorySummary());
        if (request.getInsuranceStatus() != null) profile.setInsuranceStatus(request.getInsuranceStatus());
        if (request.getInjuryStatus() != null) profile.setInjuryStatus(request.getInjuryStatus());
        profile.setBranchId(request.getBranchId() != null ? request.getBranchId() : (profile.getBranchId() != null ? profile.getBranchId() : 1L));
        
        PatientProfile saved = patientRepository.save(profile);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<java.util.List<PatientProfile>> getMyPatients() {
        return ResponseEntity.ok(java.util.List.of());
    }

    @PutMapping("/{patientId}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PatientProfile> updatePatientByDoctor(
            @PathVariable Long patientId,
            @RequestBody PatientProfileRequest request) {
        
        Optional<PatientProfile> existing = patientRepository.findById(patientId);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        PatientProfile profile = existing.get();
        if (request.getDateOfBirth() != null) profile.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) profile.setGender(request.getGender());
        if (request.getBloodGroup() != null) profile.setBloodGroup(request.getBloodGroup());
        if (request.getEmergencyContactName() != null) profile.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null) profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getAddress() != null) profile.setAddress(request.getAddress());
        if (request.getMedicalHistorySummary() != null) profile.setMedicalHistorySummary(request.getMedicalHistorySummary());
        if (request.getAllergies() != null) profile.setAllergies(request.getAllergies());
        if (request.getInsuranceStatus() != null) profile.setInsuranceStatus(request.getInsuranceStatus());
        if (request.getInjuryStatus() != null) profile.setInjuryStatus(request.getInjuryStatus());
        
        PatientProfile saved = patientRepository.save(profile);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<java.util.List<java.util.Map<String, Object>>> searchPatients(@RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(java.util.List.of());
        }
        String lowerQuery = query.toLowerCase();
        java.util.List<PatientProfile> allPatients = patientRepository.findAll();
        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        
        for (PatientProfile p : allPatients) {
            com.healthcare.clinic.identity.entity.User u = p.getUserId() != null ? 
                userRepository.findById(p.getUserId()).orElse(null) : null;
            if (u != null) {
                String fName = u.getFirstName() != null ? u.getFirstName() : "";
                String lName = u.getLastName() != null ? u.getLastName() : "";
                String fullName = (fName + " " + lName).toLowerCase();
                String phone = u.getPhoneNumber() != null ? u.getPhoneNumber() : "";
                if (fullName.contains(lowerQuery) || phone.contains(lowerQuery)) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", p.getId());
                    map.put("patientId", p.getUserId());
                    map.put("firstName", fName);
                    map.put("lastName", lName);
                    map.put("phone", phone);
                    map.put("gender", p.getGender());
                    result.add(map);
                }
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{patientId}/vitals/latest")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<com.healthcare.clinic.patient.entity.Vitals> getLatestVitals(@PathVariable Long patientId) {
        return vitalsRepository.findTopByPatientIdOrderByRecordedAtDesc(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{patientId}/vitals/history")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<java.util.List<com.healthcare.clinic.patient.entity.Vitals>> getAllVitals(@PathVariable Long patientId) {
        return ResponseEntity.ok(vitalsRepository.findByPatientIdOrderByRecordedAtDesc(patientId));
    }

    @PostMapping("/{patientId}/vitals/record")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<com.healthcare.clinic.patient.entity.Vitals> recordVitals(
            @PathVariable Long patientId,
            @RequestBody com.healthcare.clinic.patient.entity.Vitals vitals) {
        
        PatientProfile patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        
        vitals.setPatient(patient);
        vitals.setDoctorId(SecurityUtils.getCurrentUserId());
        
        com.healthcare.clinic.patient.entity.Vitals saved = vitalsRepository.save(vitals);
        return ResponseEntity.ok(saved);
    }
}
