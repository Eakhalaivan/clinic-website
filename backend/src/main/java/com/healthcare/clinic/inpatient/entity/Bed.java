package com.healthcare.clinic.inpatient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "InpatientBed")
@Table(name = "beds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Room room;

    @Column(name = "bed_number", nullable = false, length = 50)
    private String bedNumber;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "AVAILABLE"; // AVAILABLE, OCCUPIED, CLEANING, MAINTENANCE, RESERVED

    @Column(name = "bed_type", length = 50)
    private String bedType; 

    @Version
    private Long version; // Optimistic locking
}
