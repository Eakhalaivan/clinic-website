package com.healthcare.clinic.support.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "sp_complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpComplaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private SpTicket ticket;

    @Column(name = "patient_id")
    private Long patientId;

    private String severity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investigator_id")
    private User investigator;

    @Column(name = "investigation_notes", columnDefinition = "TEXT")
    private String investigationNotes;

    private String status;

    @Column(name = "resolution_offered", columnDefinition = "TEXT")
    private String resolutionOffered;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
