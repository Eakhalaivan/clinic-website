package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "communication_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunicationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "campaign_id")
    private Long campaignId;

    @Column(name = "template_id")
    private Long templateId;

    @Column(nullable = false, length = 30)
    private String channel; // EMAIL, SMS, WHATSAPP, IN_APP, PHONE

    /**
     * Direction: OUTBOUND (system→patient) or INBOUND (patient→system)
     */
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String direction = "OUTBOUND";

    /**
     * Event type: SENT, DELIVERED, OPENED, CLICKED, REPLIED, OPT_OUT, COMPLAINT, CALL_NOTE
     */
    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    /**
     * Template name / email subject ONLY — no PHI, no clinical data.
     */
    @Column(name = "content_summary", length = 500)
    private String contentSummary;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "consent_state", length = 20)
    private String consentState;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "provider_message_id", length = 200)
    private String providerMessageId;

    @Column(name = "event_timestamp", nullable = false)
    @Builder.Default
    private ZonedDateTime eventTimestamp = ZonedDateTime.now();
}
