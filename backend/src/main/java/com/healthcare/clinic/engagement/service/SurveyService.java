package com.healthcare.clinic.engagement.service;

import com.healthcare.clinic.engagement.entity.SurveyResponse;
import com.healthcare.clinic.engagement.entity.SurveyTemplate;
import com.healthcare.clinic.engagement.repository.SurveyResponseRepository;
import com.healthcare.clinic.engagement.repository.SurveyTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyTemplateRepository surveyTemplateRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    @Transactional(readOnly = true)
    public List<SurveyTemplate> getTemplatesByContext(SurveyTemplate.TriggerContext context) {
        return surveyTemplateRepository.findByTriggerContext(context);
    }

    @Transactional
    public SurveyResponse submitResponse(SurveyResponse response) {
        return surveyResponseRepository.save(response);
    }

    @Transactional(readOnly = true)
    public List<SurveyResponse> getPatientResponses(Long patientId) {
        return surveyResponseRepository.findByPatientId(patientId);
    }
}
