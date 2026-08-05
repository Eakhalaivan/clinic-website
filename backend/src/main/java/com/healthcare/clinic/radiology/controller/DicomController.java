package com.healthcare.clinic.radiology.controller;

import com.healthcare.clinic.radiology.service.DicomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/radiology/dicom")
@RequiredArgsConstructor
public class DicomController {

    private final DicomService dicomService;

    @GetMapping("/study/{studyId}")
    @PreAuthorize("hasAnyRole('RADIOLOGIST', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getStudyMetadata(@PathVariable String studyId) {
        Map<String, Object> metadata = dicomService.getStudyMetadata(studyId);
        return ResponseEntity.ok(metadata);
    }
}
