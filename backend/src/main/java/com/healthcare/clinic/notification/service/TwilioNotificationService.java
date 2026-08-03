package com.healthcare.clinic.notification.service;

import com.healthcare.clinic.appointment.event.AppointmentBookedEvent;
import com.healthcare.clinic.notification.event.AppointmentCancelledEvent;
import com.healthcare.clinic.notification.event.InvoiceCreatedEvent;
import com.healthcare.clinic.notification.event.LabResultReleasedEvent;
import com.healthcare.clinic.notification.event.QueueTokenCalledEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwilioNotificationService {

    @Value("${twilio.account-sid:}")
    private String twilioAccountSid;

    @Value("${twilio.auth-token:}")
    private String twilioAuthToken;

    @Value("${twilio.from-number:}")
    private String twilioFromNumber;

    private boolean isTwilioEnabled = false;

    @PostConstruct
    public void init() {
        if (StringUtils.hasText(twilioAccountSid) && StringUtils.hasText(twilioAuthToken) && !"dummy".equals(twilioAccountSid)) {
            try {
                Twilio.init(twilioAccountSid, twilioAuthToken);
                isTwilioEnabled = true;
                log.info("Twilio initialized successfully. SMS/WhatsApp notifications are enabled.");
            } catch (Exception e) {
                log.error("Failed to initialize Twilio: {}", e.getMessage());
            }
        } else {
            log.warn("Twilio credentials missing or dummy values used. SMS/WhatsApp notifications are disabled.");
        }
    }

    // ─── Core Sending Methods ──────────────────────────────────────────────────

    private void sendSms(String toPhone, String body) {
        if (!isTwilioEnabled || !StringUtils.hasText(toPhone)) return;
        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhone),
                    new PhoneNumber(twilioFromNumber),
                    body
            ).create();
            log.info("SMS sent successfully to {}. SID: {}", toPhone, message.getSid());
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", toPhone, e.getMessage());
        }
    }

    private void sendWhatsApp(String toPhone, String body) {
        if (!isTwilioEnabled || !StringUtils.hasText(toPhone)) return;
        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + toPhone),
                    new PhoneNumber("whatsapp:" + twilioFromNumber),
                    body
            ).create();
            log.info("WhatsApp message sent successfully to {}. SID: {}", toPhone, message.getSid());
        } catch (Exception e) {
            log.error("Failed to send WhatsApp message to {}: {}", toPhone, e.getMessage());
        }
    }

    // ─── Event Listeners / Public API ──────────────────────────────────────────
    // Note: In a real system, these would listen to @EventListener or be called by a facade.
    // Assuming the calling service passes the phone number since the event might not have it yet.

    public void sendAppointmentConfirmationSms(String patientPhone, String doctorName, String startTime) {
        String body = String.format("HealthCare Clinic: Your appointment with Dr. %s is confirmed for %s. Please arrive 10 min early.", doctorName, startTime);
        sendSms(patientPhone, body);
        sendWhatsApp(patientPhone, body); // For demo, send both if valid
    }

    public void sendAppointmentCancellationSms(String patientPhone, String doctorName, String startTime) {
        String body = String.format("HealthCare Clinic: Your appointment on %s with Dr. %s has been cancelled. Please call to reschedule.", startTime, doctorName);
        sendSms(patientPhone, body);
    }

    public void sendInvoiceCreatedSms(String patientPhone, String invoiceNumber, String totalAmount, String dueDate) {
        String body = String.format("HealthCare Clinic: Invoice %s for Rs.%s is generated. Due by %s. Please pay via portal.", invoiceNumber, totalAmount, dueDate);
        sendSms(patientPhone, body);
    }

    public void sendLabResultReleasedSms(String patientPhone, String testName) {
        String body = String.format("HealthCare Clinic: Your lab results for %s are ready. Log in to the portal to view.", testName);
        sendSms(patientPhone, body);
    }

    public void sendQueueTokenCalledSms(String patientPhone, Integer tokenNumber, String branchName) {
        String body = String.format("HealthCare Clinic: Token #%d is now being called at %s. Please proceed.", tokenNumber, branchName);
        sendSms(patientPhone, body);
    }
}
