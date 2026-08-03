package com.healthcare.clinic.clinicaldecision.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.clinicaldecision.entity.*;
import com.healthcare.clinic.clinicaldecision.repository.*;
import com.healthcare.clinic.notification.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarePathwayService {

    private final CarePathwayTemplateRepository templateRepository;
    private final PatientCarePathwayRepository pathwayRepository;
    private final CarePathwayStepRepository stepRepository;
    private final InAppNotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ── Template Management ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<CarePathwayTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }

    @Transactional
    public CarePathwayTemplate createTemplate(CarePathwayTemplate template) {
        return templateRepository.save(template);
    }

    @Transactional
    public CarePathwayTemplate updateTemplate(Long id, CarePathwayTemplate template) {
        CarePathwayTemplate existing = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Care Pathway Template not found: " + id));
        existing.setName(template.getName());
        existing.setIndication(template.getIndication());
        existing.setEstimatedDurationDays(template.getEstimatedDurationDays());
        existing.setSteps(template.getSteps());
        return templateRepository.save(existing);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }

    // ── Patient Care Pathway Assignment & Execution ───────────────────────────

    @Transactional
    public PatientCarePathway assignPathway(Long patientId, Long templateId, Long doctorId) {
        CarePathwayTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Care Pathway Template not found: " + templateId));

        LocalDate startDate = LocalDate.now();
        LocalDate targetEndDate = startDate.plusDays(template.getEstimatedDurationDays() != null ? template.getEstimatedDurationDays() : 7);

        PatientCarePathway pathway = PatientCarePathway.builder()
                .patientId(patientId)
                .templateId(templateId)
                .assignedByDoctorId(doctorId)
                .status(PathwayStatus.ACTIVE)
                .startDate(startDate)
                .targetEndDate(targetEndDate)
                .build();

        List<CarePathwayStep> stepEntities = new ArrayList<>();
        try {
            List<Map<String, Object>> rawSteps = objectMapper.readValue(
                    template.getSteps(), new TypeReference<List<Map<String, Object>>>() {});
            
            int stepNum = 1;
            for (Map<String, Object> rawStep : rawSteps) {
                String title = (String) rawStep.getOrDefault("title", "Step " + stepNum);
                String desc = (String) rawStep.getOrDefault("description", "");
                String typeStr = (String) rawStep.getOrDefault("type", "TASK");
                Integer dueOffset = (Integer) rawStep.getOrDefault("dueOffsetDays", 0);

                StepType stepType = StepType.TASK;
                try {
                    stepType = StepType.valueOf(typeStr.toUpperCase());
                } catch (Exception ignored) {}

                CarePathwayStep step = CarePathwayStep.builder()
                        .pathway(pathway)
                        .stepNumber(stepNum++)
                        .title(title)
                        .description(desc)
                        .stepType(stepType)
                        .dueOffsetDays(dueOffset != null ? dueOffset : 0)
                        .status(StepStatus.PENDING)
                        .build();

                stepEntities.add(step);
            }
        } catch (Exception e) {
            log.warn("Failed to parse steps JSON string, creating default step: {}", e.getMessage());
            stepEntities.add(CarePathwayStep.builder()
                    .pathway(pathway)
                    .stepNumber(1)
                    .title("Initial Evaluation")
                    .description("Follow clinical pathway guidelines")
                    .stepType(StepType.TASK)
                    .dueOffsetDays(0)
                    .status(StepStatus.PENDING)
                    .build());
        }

        pathway.setSteps(stepEntities);
        return pathwayRepository.save(pathway);
    }

    @Transactional(readOnly = true)
    public List<PatientCarePathway> getPathwaysForPatient(Long patientId) {
        return pathwayRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Transactional(readOnly = true)
    public PatientCarePathway getPathwayById(Long id) {
        return pathwayRepository.findWithStepsById(id)
                .orElseThrow(() -> new RuntimeException("Care Pathway not found: " + id));
    }

    @Transactional
    public CarePathwayStep startStep(Long stepId, Long userId) {
        CarePathwayStep step = stepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Care Pathway Step not found: " + stepId));
        step.setStatus(StepStatus.IN_PROGRESS);

        // NOTE ON HUMAN-NOTIFIED STEP INITIATION:
        // For APPOINTMENT or LAB_ORDER steps, instead of auto-booking a real slot/catalog item
        // without doctor/patient input, we notify the human team (Reception / Lab Desk).
        if (step.getStepType() == StepType.APPOINTMENT) {
            notificationService.sendToUser(userId, "Pathway Task: Appointment Booking Required",
                    "Please coordinate appointment booking for step: " + step.getTitle(), "CARE_PATHWAY_TASK", step.getId());
        } else if (step.getStepType() == StepType.LAB_ORDER) {
            notificationService.sendToUser(userId, "Pathway Task: Lab Order Dispatch Required",
                    "Please dispatch lab requisition for step: " + step.getTitle(), "CARE_PATHWAY_TASK", step.getId());
        }

        return stepRepository.save(step);
    }

    @Transactional
    public CarePathwayStep completeStep(Long stepId, Long userId) {
        CarePathwayStep step = stepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Care Pathway Step not found: " + stepId));
        step.setStatus(StepStatus.COMPLETED);
        step.setCompletedAt(ZonedDateTime.now());
        step.setCompletedBy(userId);

        CarePathwayStep saved = stepRepository.save(step);

        // Check if all steps in pathway are completed
        PatientCarePathway pathway = step.getPathway();
        boolean allDone = pathway.getSteps().stream()
                .allMatch(s -> s.getStatus() == StepStatus.COMPLETED || s.getStatus() == StepStatus.SKIPPED);
        if (allDone) {
            pathway.setStatus(PathwayStatus.COMPLETED);
            pathway.setActualEndDate(LocalDate.now());
            pathwayRepository.save(pathway);
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public List<CarePathwayStep> getPendingStepsForDoctor(Long doctorId) {
        return stepRepository.findDoctorPendingSteps(doctorId, List.of(StepStatus.PENDING, StepStatus.IN_PROGRESS));
    }
}
