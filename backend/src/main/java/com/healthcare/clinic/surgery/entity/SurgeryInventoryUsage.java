package com.healthcare.clinic.surgery.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "surgery_inventory_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SurgeryInventoryUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_booking_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private SurgeryBooking surgeryBooking;

    @Column(name = "item_code", nullable = false, length = 50)
    private String itemCode;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Column(nullable = false)
    private Integer quantityUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by_user_id", nullable = false)
    private User recordedBy;

    @CreatedDate
    @Column(name = "recorded_at", nullable = false, updatable = false)
    private ZonedDateTime recordedAt;
}
