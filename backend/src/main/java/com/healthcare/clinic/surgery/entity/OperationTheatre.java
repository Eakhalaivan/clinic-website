package com.healthcare.clinic.surgery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "operation_theatres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationTheatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ot_name", nullable = false, unique = true, length = 100)
    private String otName;

    @Column(length = 50)
    private String status; // AVAILABLE, IN_USE, CLEANING, MAINTENANCE

    @Column(name = "branch_id", nullable = false)
    private Long branchId;
}
