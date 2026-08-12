package com.healthcare.clinic.engagement.service;

import com.healthcare.clinic.engagement.entity.Reminder;
import com.healthcare.clinic.engagement.repository.ReminderRepository;
import com.healthcare.clinic.notification.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderGeneratorService {

    private final ReminderRepository reminderRepository;
    private final InAppNotificationService inAppNotificationService;

    // Runs every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void generateDailyReminders() {
        log.info("Starting daily reminder generation...");

        // Note: Full implementation would inject PrescriptionRepository, ImmunizationRepository, etc.
        // to sweep for upcoming due dates, follow-up dates, etc.
        // For example:
        // List<Prescription> activePrescriptions = prescriptionRepository.findActive();
        // for (Prescription p : activePrescriptions) {
        //     createMedicationReminder(p);
        // }

        deliverPendingReminders();
        
        log.info("Finished daily reminder generation.");
    }

    private void deliverPendingReminders() {
        List<Reminder> pending = reminderRepository.findByStatus(Reminder.ReminderStatus.PENDING);
        for (Reminder r : pending) {
            log.info("Delivering reminder: {}", r.getTitle());
            r.setStatus(Reminder.ReminderStatus.SENT);
            r.setChannelsSent("IN_APP");
            reminderRepository.save(r);
            // Delivery via InAppNotificationService goes here.
        }
    }
}
