package com.healthcare.clinic.engagement.repository;

import com.healthcare.clinic.engagement.entity.SurveyTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyTemplateRepository extends JpaRepository<SurveyTemplate, Long> {
    List<SurveyTemplate> findByTriggerContext(SurveyTemplate.TriggerContext triggerContext);
}
