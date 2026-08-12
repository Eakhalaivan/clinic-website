package com.healthcare.clinic.nursing.controller;

import com.healthcare.clinic.nursing.dto.MedicationIncidentRequest;
import com.healthcare.clinic.nursing.dto.NursingTaskRequest;
import com.healthcare.clinic.nursing.dto.ShiftHandoverRequest;
import com.healthcare.clinic.nursing.entity.MedicationIncident;
import com.healthcare.clinic.nursing.entity.NursingTask;
import com.healthcare.clinic.nursing.entity.ShiftHandover;
import com.healthcare.clinic.nursing.service.NursingTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nursing/tasks")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('NURSE', 'CHARGE_NURSE')")
public class NursingTaskController {

    private final NursingTaskService taskService;

    @PostMapping
    public ResponseEntity<NursingTask> createTask(@RequestBody NursingTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<NursingTask> updateTaskStatus(
            @PathVariable Long taskId, 
            @RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, payload.get("status")));
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<NursingTask>> getMyTasks() {
        return ResponseEntity.ok(taskService.getMyTasks());
    }

    @PostMapping("/handovers")
    public ResponseEntity<ShiftHandover> createHandover(@RequestBody ShiftHandoverRequest request) {
        return ResponseEntity.ok(taskService.createShiftHandover(request));
    }

    @GetMapping("/handovers/my-handovers")
    public ResponseEntity<List<ShiftHandover>> getMyHandovers() {
        return ResponseEntity.ok(taskService.getMyHandovers());
    }

    @PostMapping("/incidents")
    public ResponseEntity<MedicationIncident> reportIncident(@RequestBody MedicationIncidentRequest request) {
        return ResponseEntity.ok(taskService.reportMedicationIncident(request));
    }

    @GetMapping("/incidents/{patientId}")
    public ResponseEntity<List<MedicationIncident>> getPatientIncidents(@PathVariable Long patientId) {
        return ResponseEntity.ok(taskService.getPatientIncidents(patientId));
    }
}
