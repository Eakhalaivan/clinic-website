package com.healthcare.clinic.laboratory.repository;

import com.healthcare.clinic.laboratory.entity.LabQualityControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabQualityControlRepository extends JpaRepository<LabQualityControl, Long> {
    List<LabQualityControl> findByTestCatalogIdOrderByPerformedAtDesc(Long testCatalogId);
}
