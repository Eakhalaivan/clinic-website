package com.healthcare.clinic.vendor.entity;

import com.healthcare.clinic.backoffice.inventory.entity.BackofficePurchaseOrder;
import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "vendor_deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id", nullable = false)
    private BackofficePurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_user_id", nullable = false)
    private User vendorUser;

    @Column(name = "tracking_number", nullable = false, length = 100)
    private String trackingNumber;

    @Column(length = 100)
    private String carrier;

    @Column(name = "dispatch_date", nullable = false)
    @Builder.Default
    private LocalDate dispatchDate = LocalDate.now();

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DISPATCHED"; // DISPATCHED, IN_TRANSIT, DELIVERED

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
