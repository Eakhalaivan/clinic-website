package com.healthcare.clinic.support.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "sp_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", nullable = false, unique = true)
    private String ticketNumber;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "guest_phone")
    private String guestPhone;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String channel;
    private String category;
    private String subcategory;
    private String priority;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_agent_id")
    private User assignedAgent;

    @Column(name = "assigned_team")
    private String assignedTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_policy_id")
    private SpSlaPolicy slaPolicy;

    @Column(name = "first_response_due_at")
    private ZonedDateTime firstResponseDueAt;

    @Column(name = "resolution_due_at")
    private ZonedDateTime resolutionDueAt;

    @Column(name = "sla_status")
    private String slaStatus;

    @Column(name = "reference_appointment_id")
    private Long referenceAppointmentId;

    @Column(name = "reference_order_id")
    private Long referenceOrderId;

    @Column(name = "reference_invoice_id")
    private Long referenceInvoiceId;

    @Column(name = "resolved_at")
    private ZonedDateTime resolvedAt;

    @Column(name = "closed_at")
    private ZonedDateTime closedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
