package com.healthcare.clinic.document.service;

import com.healthcare.clinic.document.entity.Document;
import com.healthcare.clinic.document.entity.DocumentShare;
import com.healthcare.clinic.document.repository.DocumentShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final DocumentShareRepository shareRepository;
    private final DocumentService documentService;

    @Transactional
    public DocumentShare createInternalShare(Long documentId, Long sharedWithUserId, String permissionLevel, ZonedDateTime expiresAt, Long creatorId) {
        Document document = documentService.getDocumentById(documentId);
        
        DocumentShare share = DocumentShare.builder()
                .document(document)
                .sharedWithUserId(sharedWithUserId)
                .permissionLevel(permissionLevel)
                .expiresAt(expiresAt)
                .createdByUserId(creatorId)
                .build();
                
        return shareRepository.save(share);
    }

    @Transactional
    public DocumentShare createExternalShare(Long documentId, String permissionLevel, ZonedDateTime expiresAt, Long creatorId) {
        Document document = documentService.getDocumentById(documentId);
        
        DocumentShare share = DocumentShare.builder()
                .document(document)
                .permissionLevel(permissionLevel)
                .expiresAt(expiresAt)
                .createdByUserId(creatorId)
                .build();
                
        return shareRepository.save(share);
    }

    @Transactional
    public void revokeShare(Long shareId) {
        DocumentShare share = shareRepository.findById(shareId)
                .orElseThrow(() -> new IllegalArgumentException("Share not found"));
                
        share.setRevokedAt(ZonedDateTime.now());
        shareRepository.save(share);
    }

    @Transactional(readOnly = true)
    public DocumentShare validateAndGetExternalShare(String shareToken) {
        DocumentShare share = shareRepository.findByShareToken(shareToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid share token"));
                
        if (share.getRevokedAt() != null) {
            throw new IllegalStateException("This link has been revoked.");
        }
        
        if (share.getExpiresAt() != null && share.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new IllegalStateException("This link has expired.");
        }
        
        return share;
    }
}
