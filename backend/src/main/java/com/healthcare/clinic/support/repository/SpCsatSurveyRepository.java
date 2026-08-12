package com.healthcare.clinic.support.repository;

import com.healthcare.clinic.support.entity.SpCsatSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpCsatSurveyRepository extends JpaRepository<SpCsatSurvey, Long> {
}
