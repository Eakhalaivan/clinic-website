package com.healthcare.clinic.radiology.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class DicomService {

    public Map<String, Object> getStudyMetadata(String studyId) {
        log.info("Fetching DICOM metadata for study: {}", studyId);
        
        // Simulating metadata that would typically come from a PACS server
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("studyId", studyId);
        metadata.put("patientName", "Ayesha Khan");
        metadata.put("modality", "CR");
        metadata.put("studyDescription", "Chest X-Ray (PA View)");
        
        // Simulate pre-signed S3 URL or WADO-RS endpoint
        String dicomUrl = "https://example.com/pacs/wado?requestType=WADO&studyUID=" + studyId + "&objectUID=" + UUID.randomUUID().toString();
        metadata.put("wadoRsUrl", dicomUrl);
        
        return metadata;
    }
}
