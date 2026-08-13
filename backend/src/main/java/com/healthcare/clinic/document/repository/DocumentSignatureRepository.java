package com.healthcare.clinic.document.repository;

import com.healthcare.clinic.document.entity.DocumentSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentSignatureRepository extends JpaRepository<DocumentSignature, Long> {
    List<DocumentSignature> findByDocumentIdOrderBySignedAtDesc(Long documentId);
}
