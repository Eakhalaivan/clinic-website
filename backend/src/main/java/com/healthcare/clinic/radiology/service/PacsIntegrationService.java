package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.radiology.entity.DicomStudy;
import com.healthcare.clinic.radiology.entity.ImagingRequest;
import com.healthcare.clinic.radiology.repository.DicomStudyRepository;
import com.healthcare.clinic.radiology.repository.ImagingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacsIntegrationService {

    private final DicomStudyRepository studyRepository;
    private final ImagingRequestRepository requestRepository;

    @Value("${pacs.storage.path:/tmp/pacs}")
    private String pacsStoragePath;

    @Transactional
    public DicomStudy ingestDicomFile(Long requestId, MultipartFile file, String technicianId) {
        ImagingRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Imaging Request not found: " + requestId));

        if (!"CHECKED_IN".equals(request.getStatus()) && !"ACQUIRED".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot ingest DICOM for request in status: " + request.getStatus());
        }

        // Basic DICOM validation could happen here (magic number "DICM" check at offset 128)
        
        String studyInstanceUid = UUID.randomUUID().toString(); // In real world, extract from DICOM
        
        Path storageDir = Paths.get(pacsStoragePath, studyInstanceUid);
        try {
            Files.createDirectories(storageDir);
            String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : UUID.randomUUID().toString() + ".dcm";
            Path targetPath = storageDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // Check if study already exists for this request
            DicomStudy study = studyRepository.findByRequestId(requestId).orElse(null);
            if (study == null) {
                study = DicomStudy.builder()
                        .studyInstanceUid(studyInstanceUid)
                        .request(request)
                        .patient(request.getPatient())
                        .modality(request.getProcedure().getModality())
                        .status("AVAILABLE_FOR_REPORTING")
                        .storagePath(storageDir.toString())
                        .build();
                study = studyRepository.save(study);
            } else {
                study.setInstanceCount(study.getInstanceCount() + 1);
                study = studyRepository.save(study);
            }

            request.setStatus("REPORTING");
            requestRepository.save(request);

            return study;
        } catch (IOException e) {
            log.error("Failed to store DICOM file", e);
            throw new RuntimeException("Failed to store DICOM file", e);
        }
    }

    public Resource loadDicomAsResource(String studyInstanceUid, String filename) {
        try {
            Path file = Paths.get(pacsStoragePath, studyInstanceUid).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    public java.util.Optional<DicomStudy> getStudyByRequestId(Long requestId) {
        return studyRepository.findByRequestId(requestId);
    }
}
