package com.healthcare.clinic.document.service;

import com.healthcare.clinic.document.entity.Document;
import com.healthcare.clinic.document.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentStorageService storageService;

    @Transactional
    public Document uploadNewDocument(MultipartFile file, String ownerType, Long ownerId, String documentType, String title, String description, ZonedDateTime expiresAt, Long branchId, Long uploaderId) {
        String storageKey = storageService.uploadFile(file);

        Document doc = Document.builder()
                .ownerType(ownerType)
                .ownerId(ownerId)
                .documentType(documentType)
                .title(title)
                .description(description)
                .storageKey(storageKey)
                .mimeType(file.getContentType())
                .fileSizeBytes(file.getSize())
                .originalFilename(file.getOriginalFilename())
                .expiresAt(expiresAt)
                .branchId(branchId)
                .uploadedByUserId(uploaderId)
                .build();

        return documentRepository.save(doc);
    }

    @Transactional
    public Document uploadNewVersion(Long parentDocumentId, MultipartFile file, Long uploaderId) {
        Document parent = documentRepository.findById(parentDocumentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent document not found"));

        if (!"ACTIVE".equals(parent.getStatus())) {
            throw new IllegalStateException("Can only add versions to ACTIVE documents.");
        }

        String storageKey = storageService.uploadFile(file);

        // Mark previous version as SUPERSEDED
        parent.setStatus("SUPERSEDED");
        documentRepository.save(parent);

        // Create new version
        Document newVersion = Document.builder()
                .ownerType(parent.getOwnerType())
                .ownerId(parent.getOwnerId())
                .documentType(parent.getDocumentType())
                .title(parent.getTitle())
                .description(parent.getDescription())
                .storageKey(storageKey)
                .mimeType(file.getContentType())
                .fileSizeBytes(file.getSize())
                .originalFilename(file.getOriginalFilename())
                .versionNumber(parent.getVersionNumber() + 1)
                .parentDocument(parent)
                .expiresAt(parent.getExpiresAt())
                .branchId(parent.getBranchId())
                .uploadedByUserId(uploaderId)
                .build();

        return documentRepository.save(newVersion);
    }

    @Transactional(readOnly = true)
    public Page<Document> searchDocuments(String ownerType, Long ownerId, String documentType, String status, Long branchId, Pageable pageable) {
        return documentRepository.searchDocuments(ownerType, ownerId, documentType, status, branchId, pageable);
    }

    @Transactional(readOnly = true)
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
    }

    @Transactional(readOnly = true)
    public List<Document> getVersionHistory(Long id) {
        Document current = getDocumentById(id);
        // If this is a new version, the parent is present.
        // We'll search by parentDocumentId or if this is the root, search where parent is this.
        Long rootId = current.getParentDocument() != null ? current.getParentDocument().getId() : current.getId();
        return documentRepository.findByParentDocumentIdOrderByVersionNumberDesc(rootId);
    }

    @Transactional
    public void softDeleteDocument(Long id) {
        Document doc = getDocumentById(id);
        doc.setStatus("DELETED");
        documentRepository.save(doc);
        // We don't delete from storage because it's a medical record soft delete.
    }
}
