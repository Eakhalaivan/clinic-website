package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.StaffDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffDocumentRepository extends JpaRepository<StaffDocument, Long> {
    List<StaffDocument> findByStaffIdOrderByCreatedAtDesc(Long staffId);
}
