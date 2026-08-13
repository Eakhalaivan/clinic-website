package com.healthcare.clinic.document.service;

import com.healthcare.clinic.document.entity.Document;
import com.healthcare.clinic.document.entity.DocumentSignature;
import com.healthcare.clinic.document.repository.DocumentSignatureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureService {

    private final DocumentSignatureRepository signatureRepository;
    private final DocumentService documentService;
    private final DocumentStorageService storageService;

    @Transactional
    public DocumentSignature signDocument(Long documentId, Long userId, String ipAddress, String signatureNote) {
        Document document = documentService.getDocumentById(documentId);

        // Fetch the file from storage to compute its hash
        String contentHash = computeHash(document.getStorageKey());

        DocumentSignature signature = DocumentSignature.builder()
                .document(document)
                .signedByUserId(userId)
                .ipAddress(ipAddress)
                .contentHashAtSigning(contentHash)
                .signatureNote(signatureNote)
                .build();

        log.info("Document {} signed by user {} from IP {} with hash {}", documentId, userId, ipAddress, contentHash);
        return signatureRepository.save(signature);
    }

    @Transactional(readOnly = true)
    public List<DocumentSignature> getSignaturesForDocument(Long documentId) {
        return signatureRepository.findByDocumentIdOrderBySignedAtDesc(documentId);
    }

    private String computeHash(String storageKey) {
        try (InputStream is = storageService.downloadFile(storageKey)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] hash = digest.digest();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute document hash", e);
        }
    }
}
