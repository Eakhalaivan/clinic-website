package com.healthcare.clinic.clinicaldecision.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "order_set_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSetTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(name = "diagnosis_codes", nullable = false)
    @Builder.Default
    private String diagnosisCodes = "[]";

    @Column(nullable = false)
    @Builder.Default
    private String items = "[]";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
