package com.healthcare.clinic.emr.controller;

import com.healthcare.clinic.emr.entity.ExternalMedicationHistoryEntry;
import com.healthcare.clinic.emr.repository.ExternalMedicationHistoryEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emr/externalmedicationhistoryentrys")
@RequiredArgsConstructor
public class ExternalMedicationHistoryEntryController {

    private final ExternalMedicationHistoryEntryRepository repository;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ExternalMedicationHistoryEntry>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(repository.findByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<ExternalMedicationHistoryEntry> create(@RequestBody ExternalMedicationHistoryEntry entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExternalMedicationHistoryEntry> update(@PathVariable Long id, @RequestBody ExternalMedicationHistoryEntry entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

