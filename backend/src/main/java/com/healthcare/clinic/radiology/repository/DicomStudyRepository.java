package com.healthcare.clinic.radiology.repository;

import com.healthcare.clinic.radiology.entity.DicomStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DicomStudyRepository extends JpaRepository<DicomStudy, Long> {
    Optional<DicomStudy> findByStudyInstanceUid(String studyInstanceUid);
    Optional<DicomStudy> findByRequestId(Long requestId);
}
