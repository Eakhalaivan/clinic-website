package com.healthcare.clinic.radiology.repository;

import com.healthcare.clinic.radiology.entity.RadiologyAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RadiologyAnnotationRepository extends JpaRepository<RadiologyAnnotation, Long> {

    List<RadiologyAnnotation> findByStudyId(Long studyId);
    
    List<RadiologyAnnotation> findByStudyIdAndStatus(Long studyId, String status);
}
