package com.healthcare.clinic.inpatient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Ward ward;

    @Column(name = "room_number", nullable = false, length = 50)
    private String roomNumber;

    @Column(name = "room_type", nullable = false, length = 50)
    private String roomType; // GENERAL, SEMI_PRIVATE, PRIVATE, ICU, ISOLATION

    @Column(nullable = false)
    @Builder.Default
    private Integer capacity = 0;
}
