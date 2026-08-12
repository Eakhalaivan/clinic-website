package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.doctor.entity.ClinicalAttachment;
import com.healthcare.clinic.doctor.repository.ClinicalAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicalAttachmentService {

    private final ClinicalAttachmentRepository attachmentRepository;
    private final String UPLOAD_DIR = System.getProperty("java.io.tmpdir") + "/clinic-uploads/";

    @Transactional
    public ClinicalAttachment uploadAttachment(Long patientId, Long encounterId, Long uploadedBy, String documentType, String description, MultipartFile file) throws IOException {
        // Ensure directory exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ClinicalAttachment attachment = new ClinicalAttachment();
        attachment.setPatientId(patientId);
        attachment.setEncounterId(encounterId);
        attachment.setUploadedBy(uploadedBy);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setFilePath(filePath.toString());
        attachment.setDocumentType(documentType);
        attachment.setDescription(description);

        return attachmentRepository.save(attachment);
    }

    public List<ClinicalAttachment> getAttachmentsForPatient(Long patientId) {
        return attachmentRepository.findByPatientId(patientId);
    }

    public List<ClinicalAttachment> getAttachmentsForEncounter(Long encounterId) {
        return attachmentRepository.findByEncounterId(encounterId);
    }
}
