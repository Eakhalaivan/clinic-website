package com.healthcare.clinic.clinicaldecision.repository;

import com.healthcare.clinic.clinicaldecision.entity.CarePathwayTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarePathwayTemplateRepository extends JpaRepository<CarePathwayTemplate, Long> {
    List<CarePathwayTemplate> findByIndicationContainingIgnoreCase(String indication);
}
