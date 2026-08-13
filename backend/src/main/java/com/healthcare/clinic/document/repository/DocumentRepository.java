package com.healthcare.clinic.document.repository;

import com.healthcare.clinic.document.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE " +
           "(:ownerType IS NULL OR d.ownerType = :ownerType) AND " +
           "(:ownerId IS NULL OR d.ownerId = :ownerId) AND " +
           "(:documentType IS NULL OR d.documentType = :documentType) AND " +
           "(:status IS NULL OR d.status = :status) AND " +
           "(:branchId IS NULL OR d.branchId = :branchId)")
    Page<Document> searchDocuments(String ownerType, Long ownerId, String documentType, String status, Long branchId, Pageable pageable);

    List<Document> findByParentDocumentIdOrderByVersionNumberDesc(Long parentDocumentId);
    
    Optional<Document> findByIdAndOwnerIdAndOwnerType(Long id, Long ownerId, String ownerType);
}
