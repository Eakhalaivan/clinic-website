package com.healthcare.clinic.medicalrecord.controller;

import com.healthcare.clinic.medicalrecord.dto.MedicalRecordRequest;
import com.healthcare.clinic.medicalrecord.dto.MedicalRecordResponse;
import com.healthcare.clinic.medicalrecord.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("(hasAuthority('ROLE_PATIENT') and @securityUtils.isSameUser(#patientId)) or hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<MedicalRecordResponse>> getPatientRecords(@PathVariable Long patientId) {
        List<MedicalRecordResponse> records = medicalRecordService.getRecordsForPatient(patientId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') and @securityUtils.isSameUser(#doctorId)")
    public ResponseEntity<List<MedicalRecordResponse>> getDoctorRecords(@PathVariable Long doctorId) {
        List<MedicalRecordResponse> records = medicalRecordService.getRecordsByDoctor(doctorId);
        return ResponseEntity.ok(records);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<MedicalRecordResponse> createRecord(@Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecordResponse created = medicalRecordService.createRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        MedicalRecordResponse record = medicalRecordService.getRecordById(id);
        Long currentUserId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"));
        
        if (!isAdmin && (currentUserId == null || !record.getDoctorId().equals(currentUserId))) {
            throw new org.springframework.security.access.AccessDeniedException("You do not have permission to delete this record");
        }

        medicalRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}/clinical-notes")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<com.healthcare.clinic.medicalrecord.entity.ClinicalNote>> getPatientClinicalNotes(
            @PathVariable Long patientId,
            @org.springframework.beans.factory.annotation.Autowired com.healthcare.clinic.medicalrecord.repository.ClinicalNoteRepository noteRepository) {
        return ResponseEntity.ok(noteRepository.findByPatientIdOrderByCreatedAtDesc(patientId));
    }

    @PostMapping("/patient/{patientId}/clinical-notes")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<com.healthcare.clinic.medicalrecord.entity.ClinicalNote> createClinicalNote(
            @PathVariable Long patientId,
            @RequestBody com.healthcare.clinic.medicalrecord.entity.ClinicalNote note,
            @org.springframework.beans.factory.annotation.Autowired com.healthcare.clinic.medicalrecord.repository.ClinicalNoteRepository noteRepository) {
        note.setPatientId(patientId);
        Long doctorId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        note.setDoctorId(doctorId != null ? doctorId : 1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteRepository.save(note));
    }
}
