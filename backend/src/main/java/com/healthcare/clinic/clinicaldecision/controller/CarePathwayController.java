package com.healthcare.clinic.clinicaldecision.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.clinicaldecision.entity.CarePathwayStep;
import com.healthcare.clinic.clinicaldecision.entity.CarePathwayTemplate;
import com.healthcare.clinic.clinicaldecision.entity.PatientCarePathway;
import com.healthcare.clinic.clinicaldecision.service.CarePathwayService;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/care-pathways")
@RequiredArgsConstructor
public class CarePathwayController {

    private final CarePathwayService pathwayService;

    // Template Endpoints
    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<CarePathwayTemplate>>> getAllTemplates() {
        return ResponseEntity.ok(ApiResponse.success(pathwayService.getAllTemplates()));
    }

    @PostMapping("/templates")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<ApiResponse<CarePathwayTemplate>> createTemplate(@RequestBody CarePathwayTemplate template) {
        return ResponseEntity.ok(ApiResponse.success(pathwayService.createTemplate(template), "Care Pathway Template created"));
    }

    @PutMapping("/templates/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<ApiResponse<CarePathwayTemplate>> updateTemplate(@PathVariable Long id, @RequestBody CarePathwayTemplate template) {
        return ResponseEntity.ok(ApiResponse.success(pathwayService.updateTemplate(id, template), "Care Pathway Template updated"));
    }

    @DeleteMapping("/templates/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long id) {
        pathwayService.deleteTemplate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Care Pathway Template deleted"));
    }

    // Pathway Assignment & Steps Endpoints
    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PatientCarePathway>> assignPathway(@RequestBody AssignPathwayRequest request) {
        Long doctorId = SecurityUtils.getCurrentUserId();
        PatientCarePathway pathway = pathwayService.assignPathway(request.getPatientId(), request.getTemplateId(), doctorId);
        return ResponseEntity.ok(ApiResponse.success(pathway, "Care Pathway assigned to patient"));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN') or @securityUtils.isSameUser(#patientId)")
    public ResponseEntity<ApiResponse<List<PatientCarePathway>>> getPathwaysForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success(pathwayService.getPathwaysForPatient(patientId)));
    }

    @PostMapping("/steps/{stepId}/start")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CarePathwayStep>> startStep(@PathVariable Long stepId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(pathwayService.startStep(stepId, userId), "Pathway step set to IN_PROGRESS"));
    }

    @PostMapping("/steps/{stepId}/complete")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CarePathwayStep>> completeStep(@PathVariable Long stepId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(pathwayService.completeStep(stepId, userId), "Pathway step completed"));
    }

    @GetMapping("/doctor/{doctorId}/pending")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<CarePathwayStep>>> getDoctorPendingSteps(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.success(pathwayService.getPendingStepsForDoctor(doctorId)));
    }
}

@Data
class AssignPathwayRequest {
    private Long patientId;
    private Long templateId;
}
