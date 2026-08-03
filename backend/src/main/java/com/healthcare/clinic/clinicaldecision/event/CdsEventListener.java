package com.healthcare.clinic.clinicaldecision.event;

import com.healthcare.clinic.clinicaldecision.entity.AlertStatus;
import com.healthcare.clinic.clinicaldecision.entity.CdsAlert;
import com.healthcare.clinic.clinicaldecision.entity.CdsRule;
import com.healthcare.clinic.clinicaldecision.entity.TriggerEvent;
import com.healthcare.clinic.clinicaldecision.repository.CdsAlertRepository;
import com.healthcare.clinic.clinicaldecision.repository.CdsRuleRepository;
import com.healthcare.clinic.notification.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Async Event Listener for non-blocking CDS Rule evaluation.
 * Triggered AFTER domain transactions complete (fire-and-forget).
 * Generates advisory CdsAlert records and in-app notifications.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CdsEventListener {

    private final CdsRuleRepository ruleRepository;
    private final CdsAlertRepository alertRepository;
    private final InAppNotificationService notificationService;

    @Async
    @EventListener
    public void handlePrescriptionCreated(PrescriptionCreatedEvent event) {
        log.info("Async CDS Rule evaluation for PrescriptionCreatedEvent on patientId: {}", event.getPatientId());
        List<CdsRule> rules = ruleRepository.findByTriggerEventAndIsActiveTrue(TriggerEvent.ON_PRESCRIPTION);
        
        for (CdsRule rule : rules) {
            String alertMessage = "CDS Rule [" + rule.getName() + "]: Advisory review for prescription medications "
                    + event.getMedicationNames() + ". " + (rule.getDescription() != null ? rule.getDescription() : "");
            
            CdsAlert alert = CdsAlert.builder()
                    .patientId(event.getPatientId())
                    .ruleId(rule.getId())
                    .triggeredByUserId(event.getDoctorId())
                    .message(alertMessage)
                    .severity(rule.getSeverity())
                    .status(AlertStatus.PENDING)
                    .build();
            
            alertRepository.save(alert);

            if (event.getDoctorId() != null) {
                try {
                    notificationService.sendToUser(
                            event.getDoctorId(),
                            "Clinical Decision Alert: " + rule.getName(),
                            alertMessage,
                            "CDS_ALERT",
                            alert.getId()
                    );
                } catch (Exception e) {
                    log.warn("Failed to dispatch in-app notification for CDS alert: {}", e.getMessage());
                }
            }
        }
    }

    @Async
    @EventListener
    public void handleLabTestOrdered(LabTestOrderedEvent event) {
        log.info("Async CDS Rule evaluation for LabTestOrderedEvent on patientId: {}", event.getPatientId());
        List<CdsRule> rules = ruleRepository.findByTriggerEventAndIsActiveTrue(TriggerEvent.ON_LAB_ORDER);

        for (CdsRule rule : rules) {
            String alertMessage = "CDS Rule [" + rule.getName() + "]: Advisory review for lab order '"
                    + event.getTestName() + "'. " + (rule.getDescription() != null ? rule.getDescription() : "");

            CdsAlert alert = CdsAlert.builder()
                    .patientId(event.getPatientId())
                    .ruleId(rule.getId())
                    .triggeredByUserId(event.getDoctorId())
                    .message(alertMessage)
                    .severity(rule.getSeverity())
                    .status(AlertStatus.PENDING)
                    .build();

            alertRepository.save(alert);
        }
    }

    @Async
    @EventListener
    public void handleDiagnosisAdded(DiagnosisAddedEvent event) {
        log.info("Async CDS Rule evaluation for DiagnosisAddedEvent on patientId: {}", event.getPatientId());
        List<CdsRule> rules = ruleRepository.findByTriggerEventAndIsActiveTrue(TriggerEvent.ON_DIAGNOSIS);

        for (CdsRule rule : rules) {
            String alertMessage = "CDS Rule [" + rule.getName() + "]: Suggested pathway/order set for diagnosis "
                    + event.getIcd10Code() + " (" + event.getDiagnosisName() + "). "
                    + (rule.getDescription() != null ? rule.getDescription() : "");

            CdsAlert alert = CdsAlert.builder()
                    .patientId(event.getPatientId())
                    .ruleId(rule.getId())
                    .triggeredByUserId(event.getDoctorId())
                    .message(alertMessage)
                    .severity(rule.getSeverity())
                    .status(AlertStatus.PENDING)
                    .build();

            alertRepository.save(alert);
        }
    }
}
