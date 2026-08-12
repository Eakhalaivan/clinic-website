package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.radiology.entity.DicomStudy;
import com.healthcare.clinic.radiology.entity.ImagingRequest;
import com.healthcare.clinic.radiology.entity.RadiologyAccessLog;
import com.healthcare.clinic.radiology.repository.DicomStudyRepository;
import com.healthcare.clinic.radiology.repository.ImagingRequestRepository;
import com.healthcare.clinic.radiology.repository.RadiologyAccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DicomService {

    private final DicomStudyRepository dicomStudyRepository;
    private final ImagingRequestRepository imagingRequestRepository;
    private final RadiologyAccessLogRepository accessLogRepository;

    @Transactional
    public DicomStudy saveStudyMock(Long requestId, String modality, User uploader) {
        ImagingRequest request = imagingRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
                
        // In a real system, the DICOM files are parsed to extract this. Here we mock it.
        DicomStudy study = DicomStudy.builder()
                .studyInstanceUid("1.2.840.113619.2." + UUID.randomUUID().toString())
                .accessionNumber("ACC" + request.getId())
                .request(request)
                .patient(request.getPatient())
                .modality(modality)
                .seriesCount(1)
                .instanceCount(10)
                .storagePath("/pacs/mock/" + request.getId())
                .build();
                
        study = dicomStudyRepository.save(study);
        
        request.setStatus("IMAGE_ACQUIRED");
        imagingRequestRepository.save(request);
        
        return study;
    }

    @Transactional
    public Map<String, Object> getStudyMetadata(String studyInstanceUid, User user, String ipAddress) {
        log.info("Fetching DICOM metadata for study: {} by user: {}", studyInstanceUid, user.getUsername());
        
        DicomStudy study = dicomStudyRepository.findByStudyInstanceUid(studyInstanceUid)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
                
        // Authorization check: User must be SuperAdmin, Radiologist, the ordering Doctor, or the Patient
        boolean isSuperAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"));
        boolean isRadiologist = user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_RADIOLOGIST"));
        boolean isOrderingDoctor = study.getRequest().getDoctor() != null && study.getRequest().getDoctor().getUserId().equals(user.getId());
        boolean isPatient = study.getPatient().getUserId().equals(user.getId());
        
        if (!isSuperAdmin && !isRadiologist && !isOrderingDoctor && !isPatient) {
            log.warn("Unauthorized DICOM access attempt by user: {}", user.getUsername());
            throw new SecurityException("Forbidden: You do not have access to this study.");
        }
        
        // If patient, only allow if report is released
        if (isPatient && !("RELEASED".equals(study.getRequest().getStatus()))) {
            log.warn("Patient attempted to access unreleased study: {}", studyInstanceUid);
            throw new SecurityException("Forbidden: Study is not yet released.");
        }
        
        // Log access
        RadiologyAccessLog logEntry = RadiologyAccessLog.builder()
                .user(user)
                .request(study.getRequest())
                .dicomStudy(study)
                .accessType("VIEW_DICOM_STUDY")
                .ipAddress(ipAddress)
                .build();
        accessLogRepository.save(logEntry);
        
        // Construct mock WADO-RS response
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("studyInstanceUid", study.getStudyInstanceUid());
        metadata.put("patientId", study.getPatient().getId());
        metadata.put("patientName", "Patient " + study.getPatient().getId()); // Simplification since user join is not mapped here
        metadata.put("modality", study.getModality());
        metadata.put("seriesCount", study.getSeriesCount());
        metadata.put("instanceCount", study.getInstanceCount());
        metadata.put("wadoRsUrl", "/api/radiology/dicom/wado/" + study.getStudyInstanceUid());
        
        return metadata;
    }

    @Transactional
    public Map<String, Object> getStudyMetadataByRequestId(Long requestId, User user, String ipAddress) {
        log.info("Fetching DICOM metadata for requestId: {} by user: {}", requestId, user.getUsername());
        
        DicomStudy study = dicomStudyRepository.findByRequestId(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found for request"));
                
        return getStudyMetadata(study.getStudyInstanceUid(), user, ipAddress);
    }
}
