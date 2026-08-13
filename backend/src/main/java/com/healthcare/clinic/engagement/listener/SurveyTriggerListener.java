package com.healthcare.clinic.engagement.listener;

import com.healthcare.clinic.appointment.event.AppointmentCompletedEvent;
import com.healthcare.clinic.engagement.entity.SurveyTemplate;
import com.healthcare.clinic.engagement.service.SurveyService;
import com.healthcare.clinic.notification.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SurveyTriggerListener {

    private final SurveyService surveyService;
    private final InAppNotificationService inAppNotificationService;

    @Async
    @EventListener
    public void handleAppointmentCompleted(AppointmentCompletedEvent event) {
        log.info("Triggering post-appointment surveys for Appointment ID {}", event.getAppointmentId());

        List<SurveyTemplate> templates = surveyService.getTemplatesByContext(SurveyTemplate.TriggerContext.POST_APPOINTMENT);
        
        if (!templates.isEmpty()) {
            // Note: In a real system, you would find the PatientId from the Appointment.
            // For now, we are simulating the trigger. We can send a generic notification 
            // or we'd need to inject AppointmentRepository to look it up.
            log.info("Found {} survey templates for post-appointment. Would send notification here.", templates.size());
        }
    }
}
