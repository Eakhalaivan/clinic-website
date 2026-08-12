package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.NpsResponse;
import com.healthcare.clinic.marketing.entity.NpsSurvey;
import com.healthcare.clinic.marketing.repository.NpsResponseRepository;
import com.healthcare.clinic.marketing.repository.NpsSurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NpsService {

    private final NpsSurveyRepository surveyRepository;
    private final NpsResponseRepository responseRepository;

    private static final int LOW_NPS_THRESHOLD = 7; // 0-6 are detractors

    /**
     * Creates an NPS survey for a completed appointment or order.
     * Idempotent — if a survey already exists for this event, returns it without creating a duplicate.
     */
    @Transactional
    public NpsSurvey createSurveyForEvent(String eventType, Long eventId, Long patientId,
                                           Long branchId, Long serviceId, Long doctorId) {
        String idempotencyKey = eventType + "_" + eventId;

        // Idempotency — prevent duplicate surveys per event
        return surveyRepository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> {
                    NpsSurvey survey = NpsSurvey.builder()
                            .appointmentId("APPOINTMENT".equals(eventType) ? eventId : null)
                            .orderId("ORDER".equals(eventType) ? eventId : null)
                            .patientId(patientId)
                            .branchId(branchId)
                            .serviceId(serviceId)
                            .doctorId(doctorId)
                            .status("PENDING")
                            .idempotencyKey(idempotencyKey)
                            .build();
                    return surveyRepository.save(survey);
                });
    }

    /**
     * Marks survey as sent (outbox/email/in-app trigger).
     */
    @Transactional
    public NpsSurvey markSent(Long surveyId) {
        NpsSurvey survey = getSurveyById(surveyId);
        if ("PENDING".equals(survey.getStatus())) {
            survey.setStatus("SENT");
            survey.setSentAt(ZonedDateTime.now());
            return surveyRepository.save(survey);
        }
        return survey;
    }

    /**
     * Submits a patient's NPS response.
     * Prevents duplicate responses (unique constraint on survey_id).
     * Escalates negative scores automatically.
     */
    @Transactional
    public NpsResponse submitResponse(Long surveyId, Integer npsScore, Integer rating,
                                       String comments, String category) {
        NpsSurvey survey = getSurveyById(surveyId);

        if ("COMPLETED".equals(survey.getStatus())) {
            throw new IllegalStateException("Survey has already been completed");
        }

        // Check for existing response (unique constraint will catch concurrent duplicates)
        if (responseRepository.findBySurveyId(surveyId).isPresent()) {
            throw new IllegalStateException("Survey has already been responded to");
        }

        NpsResponse response = NpsResponse.builder()
                .surveyId(surveyId)
                .npsScore(npsScore)
                .rating(rating)
                .comments(comments)
                .category(category)
                .escalationStatus("NONE")
                .submittedAt(ZonedDateTime.now())
                .build();

        // Auto-escalate low NPS scores
        if (npsScore != null && npsScore < LOW_NPS_THRESHOLD) {
            response.setEscalationStatus("ESCALATED");
            log.info("NPS survey {} for patient {} scored {} — escalating for review",
                    surveyId, survey.getPatientId(), npsScore);
        }

        survey.setStatus("COMPLETED");
        survey.setCompletedAt(ZonedDateTime.now());
        surveyRepository.save(survey);

        return responseRepository.save(response);
    }

    @Transactional
    public NpsResponse resolveEscalation(Long surveyId, Long resolvedBy, String resolutionNotes) {
        NpsResponse response = responseRepository.findBySurveyId(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("No response for survey: " + surveyId));
        response.setEscalationStatus("RESOLVED");
        response.setResolvedAt(ZonedDateTime.now());
        response.setResolutionNotes(resolutionNotes);
        response.setEscalatedTo(resolvedBy);
        return responseRepository.save(response);
    }

    @Transactional(readOnly = true)
    public Page<NpsSurvey> getSurveysForBranch(Long branchId, Pageable pageable) {
        return surveyRepository.findByBranchIdOrderByCreatedAtDesc(branchId, pageable);
    }

    @Transactional(readOnly = true)
    public Double getAverageNpsForBranch(Long branchId) {
        return responseRepository.averageNpsScoreForBranch(branchId);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private NpsSurvey getSurveyById(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found: " + surveyId));
    }
}
