package com.healthcare.clinic.pharmacy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @Column(name = "batch_id")
    private String batchId;

    @Column(name = "movement_type", nullable = false)
    private String movementType; // DISPENSE, GRN, RETURN, ADJUSTMENT, EXPIRED

    @Column(nullable = false)
    private Integer quantity; // positive for IN, negative for OUT

    @Column(name = "reference_id")
    private String referenceId; // e.g., prescriptionId, grnNumber

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
