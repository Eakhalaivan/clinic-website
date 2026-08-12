package com.healthcare.clinic.engagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "preventive_care_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PreventiveCareRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer minAge;
    private Integer maxAge;
    private String gender;
    private String conditionCriteria;

    @Column(nullable = false)
    private String reminderTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reminderMessage;

    @Column(nullable = false)
    private Integer intervalDays;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;
}
