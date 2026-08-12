package com.healthcare.clinic.patient.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientDocument;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientDocumentRepository;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientDocumentService {

    private final PatientDocumentRepository documentRepository;
    private final PatientProfileRepository patientProfileRepository;

    private PatientProfile getPatientProfile(User user) {
        return patientProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user"));
    }

    public List<PatientDocument> getPatientDocuments(User user) {
        PatientProfile profile = getPatientProfile(user);
        return documentRepository.findByPatientIdOrderByUploadedAtDesc(profile.getId());
    }

    @Transactional
    public PatientDocument saveDocumentMetadata(User user, PatientDocument document) {
        PatientProfile profile = getPatientProfile(user);
        
        document.setPatientId(profile.getId());
        
        // In a real application, this would handle the file upload to S3 or a local directory
        // and set the URL. We assume the URL is already set or mocked by the controller/client.
        if (document.getFileUrl() == null || document.getFileUrl().isEmpty()) {
            document.setFileUrl("https://example.com/mock-document-url.pdf");
        }

        return documentRepository.save(document);
    }
}
