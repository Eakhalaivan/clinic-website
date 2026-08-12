package com.healthcare.clinic.radiology.entity;

import com.healthcare.clinic.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "radiology_inventory_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RadiologyInventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName; // e.g. "Iodine Contrast 50ml", "Lead Apron"

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    @Column(name = "minimum_threshold", nullable = false)
    @Builder.Default
    private Integer minimumThreshold = 10;

    @Column(nullable = false, length = 20)
    private String unit; // e.g. "vials", "boxes"

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column(name = "expiry_date")
    private ZonedDateTime expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
