package com.healthcare.clinic.support.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "sp_ticket_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpTicketAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private SpTicket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_agent_id")
    private User previousAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_agent_id")
    private User newAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_id")
    private User assignedBy;

    private String reason;

    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false)
    private ZonedDateTime assignedAt;
}
