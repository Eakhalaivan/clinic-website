package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.ReceptionDocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionDocumentUploadRepository extends JpaRepository<ReceptionDocumentUpload, Long> {
    List<ReceptionDocumentUpload> findByBranchId(Long branchId);
}
