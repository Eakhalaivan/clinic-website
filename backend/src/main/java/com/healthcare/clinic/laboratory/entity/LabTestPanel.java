package com.healthcare.clinic.laboratory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lab_test_panels", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"panel_id", "test_id"}, name = "uk_panel_test")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTestPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panel_id", nullable = false)
    private LabTestCatalog panel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private LabTestCatalog test;
}
