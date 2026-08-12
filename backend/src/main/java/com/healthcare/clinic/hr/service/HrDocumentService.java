package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.EmployeeDocument;
import com.healthcare.clinic.hr.repository.EmployeeDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HrDocumentService {

    private final EmployeeDocumentRepository documentRepository;

    public HrDocumentService(EmployeeDocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public EmployeeDocument uploadDocument(EmployeeDocument document) {
        return documentRepository.save(document);
    }

    public List<EmployeeDocument> getEmployeeDocuments(Long employeeId) {
        return documentRepository.findByEmployeeId(employeeId);
    }

    public EmployeeDocument verifyDocument(Long documentId, Long verifiedById) {
        EmployeeDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID"));
        
        document.setIsVerified(true);
        document.setVerifiedBy(verifiedById);
        return documentRepository.save(document);
    }
}
