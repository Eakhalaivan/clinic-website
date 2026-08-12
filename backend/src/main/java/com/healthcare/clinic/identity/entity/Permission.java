package com.healthcare.clinic.identity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "action_type", length = 50)
    private String action; // e.g. VIEW, CREATE, UPDATE, DELETE

    @Column(name = "resource_type", length = 100)
    private String resource; // e.g. APPOINTMENT, PATIENT, INVENTORY

    @Column(length = 255)
    private String description;
}
