package com.healthcare.clinic.hr.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;

@Entity
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_requisition_id", nullable = false)
    private JobRequisition jobRequisition;

    @Column(name = "applicant_name", nullable = false, length = 100)
    private String applicantName;

    @Column(name = "applicant_email", nullable = false, length = 100)
    private String applicantEmail;

    @Column(name = "applicant_phone", length = 20)
    private String applicantPhone;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "APPLIED"; // APPLIED, SCREENING, INTERVIEW, OFFER, ACCEPTED, JOINED, REJECTED, WITHDRAWN

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "interview_notes")
    @Builder.Default
    private String interviewNotes = "[]";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
