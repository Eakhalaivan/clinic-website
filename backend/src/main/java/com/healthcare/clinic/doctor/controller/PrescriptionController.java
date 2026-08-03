package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.dto.PrescriptionRequest;
import com.healthcare.clinic.doctor.dto.PrescriptionResponse;
import com.healthcare.clinic.doctor.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final com.healthcare.clinic.doctor.service.PrescriptionTemplateService prescriptionTemplateService;

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN') or (hasAuthority('ROLE_PATIENT') and principal.userId == #patientId)")
    public ResponseEntity<List<PrescriptionResponse>> getPatientPrescriptions(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsForPatient(patientId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PATIENT') or hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PrescriptionResponse> getPrescription(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<PrescriptionResponse> createPrescription(@Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.createPrescription(request));
    }

    @GetMapping("/templates")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<com.healthcare.clinic.doctor.entity.PrescriptionTemplate>> getPrescriptionTemplates(
            @RequestParam(required = false) String category) {
        Long doctorId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(prescriptionTemplateService.getTemplatesByDoctor(doctorId, category));
    }

    @GetMapping("/templates/{id}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<com.healthcare.clinic.doctor.entity.PrescriptionTemplate> getPrescriptionTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionTemplateService.getTemplateById(id));
    }

    @PostMapping("/templates")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<com.healthcare.clinic.doctor.entity.PrescriptionTemplate> createPrescriptionTemplate(
            @RequestBody com.healthcare.clinic.doctor.entity.PrescriptionTemplate template) {
        Long doctorId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionTemplateService.createTemplate(doctorId, template));
    }

    @PutMapping("/templates/{id}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<com.healthcare.clinic.doctor.entity.PrescriptionTemplate> updatePrescriptionTemplate(
            @PathVariable Long id, @RequestBody com.healthcare.clinic.doctor.entity.PrescriptionTemplate template) {
        Long doctorId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(prescriptionTemplateService.updateTemplate(doctorId, id, template));
    }

    @DeleteMapping("/templates/{id}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<Void> deletePrescriptionTemplate(@PathVariable Long id) {
        Long doctorId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        prescriptionTemplateService.deleteTemplate(doctorId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/void")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<PrescriptionResponse> voidPrescription(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        String reason = body.getOrDefault("reason", "No reason provided");
        return ResponseEntity.ok(prescriptionService.voidPrescription(id, reason));
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAuthority('ROLE_PATIENT') or hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<byte[]> downloadPrescriptionPdf(@PathVariable Long id) {
        byte[] pdf = prescriptionService.generatePdf(id);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "prescription_" + id + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @PostMapping("/draft")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<PrescriptionResponse> saveDraft(@Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.saveDraft(request));
    }

    @PutMapping("/{id}/draft")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<PrescriptionResponse> updateDraft(@PathVariable Long id, @Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok(prescriptionService.updateDraft(id, request));
    }

    @PostMapping("/{id}/send")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<PrescriptionResponse> sendPrescription(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.sendPrescription(id));
    }


    @PostMapping("/safety-check")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<java.util.Map<String, Object>> performSafetyCheck(@RequestBody java.util.Map<String, Object> request) {
        Long patientId = Long.valueOf(request.get("patientId").toString());
        List<String> medicationNames = (List<String>) request.get("medicationNames");
        try {
            prescriptionService.performSafetyCheckOnly(patientId, medicationNames);
            return ResponseEntity.ok(java.util.Map.of("safe", true, "messages", List.of("No major interactions found.\nPrescription is safe to proceed.")));
        } catch (com.healthcare.clinic.clinicaldecision.exception.CdsCriticalSafetyException e) {
            return ResponseEntity.ok(java.util.Map.of("safe", false, "messages", e.getSafetyAlerts()));
        } catch (Exception e) {
            return ResponseEntity.ok(java.util.Map.of("safe", false, "messages", List.of(e.getMessage())));
        }
    }
}
