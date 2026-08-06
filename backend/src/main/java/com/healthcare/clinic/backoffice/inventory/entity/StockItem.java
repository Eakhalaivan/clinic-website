package com.healthcare.clinic.backoffice.inventory.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "stock_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "item_type", nullable = false, length = 20)
    @Builder.Default
    private String itemType = "SUPPLY"; // 'MEDICINE' | 'SUPPLY'

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(length = 100)
    private String sku;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String unit = "PCS";

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    @Column(name = "reorder_level", nullable = false)
    @Builder.Default
    private Integer reorderLevel = 10;

    @Column(name = "medicine_batch_id")
    private Long medicineBatchId;

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;
}
