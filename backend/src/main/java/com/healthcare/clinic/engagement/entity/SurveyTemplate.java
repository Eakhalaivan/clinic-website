package com.healthcare.clinic.engagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "survey_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SurveyTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TriggerContext triggerContext;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column()
    private String questions;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    public enum TriggerContext {
        POST_APPOINTMENT, POST_ADMISSION, POST_SURGERY, GENERAL
    }
}
