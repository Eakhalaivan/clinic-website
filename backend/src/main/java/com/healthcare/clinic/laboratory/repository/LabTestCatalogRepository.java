package com.healthcare.clinic.laboratory.repository;

import com.healthcare.clinic.laboratory.entity.LabTestCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabTestCatalogRepository extends JpaRepository<LabTestCatalog, Long> {
    List<LabTestCatalog> findByIsActiveTrue();
}
