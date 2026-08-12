package com.healthcare.clinic.laboratory.event;

import com.healthcare.clinic.clinicaldecision.event.LabTestOrderedEvent;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.notification.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LabEventListener {

    private final InAppNotificationService notificationService;
    private final UserRepository userRepository;

    @Async
    @EventListener
    public void handleLabTestOrdered(LabTestOrderedEvent event) {
        log.info("LabTestOrderedEvent received for request {}", event.getLabRequestId());
        
        // Notify lab techs
        List<User> labUsers = new java.util.ArrayList<>();
        labUsers.addAll(userRepository.findUsersByRoleName("ROLE_LAB"));
        labUsers.addAll(userRepository.findUsersByRoleName("ROLE_LAB_TECH"));
        String message = "New lab request for " + event.getTestName() + " (Patient ID: " + event.getPatientId() + ")";
        
        for (User user : labUsers) {
            try {
                notificationService.sendToUser(
                    user.getId(),
                    "New Lab Request",
                    message,
                    "NEW_LAB_REQUEST",
                    event.getLabRequestId()
                );
            } catch (Exception e) {
                log.warn("Failed to send new lab request notification to user {}: {}", user.getId(), e.getMessage());
            }
        }
    }

    @Async
    @EventListener
    public void handleLabResultCritical(LabResultCriticalEvent event) {
        log.warn("CRITICAL Lab Result for request {}", event.getLabRequestId());
        
        if (event.getDoctorId() != null) {
            String message = "CRITICAL result for " + event.getTestName() + ". Value: " + event.getResultValue();
            try {
                notificationService.sendToUser(
                    event.getDoctorId(),
                    "CRITICAL Lab Result",
                    message,
                    "CRITICAL_LAB_RESULT",
                    event.getLabRequestId()
                );
            } catch (Exception e) {
                log.warn("Failed to send critical result notification to doctor {}: {}", event.getDoctorId(), e.getMessage());
            }
        }
    }
}
