package com.healthcare.clinic.surgery.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.Map;

@Entity
@Table(name = "pre_op_checklists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PreOpChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_booking_id", nullable = false, unique = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private SurgeryBooking surgeryBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by_user_id", nullable = false)
    private User completedBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "checklist_data", columnDefinition = "jsonb")
    private Map<String, Boolean> checklistData; // e.g. {"consentSigned": true, "fastingConfirmed": true, "siteMarked": true}

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "completed_at", nullable = false, updatable = false)
    private ZonedDateTime completedAt;
}
