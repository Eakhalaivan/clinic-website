package com.healthcare.clinic.inventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_transfer_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    @JsonIgnore
    private InventoryTransfer inventoryTransfer;

    @Column(name = "item_id", nullable = false)
    private Long itemId; // Generic reference to product/medicine ID

    @Column(name = "item_type", nullable = false, length = 50)
    private String itemType; // e.g., PHARMACY_MEDICINE, LAB_REAGENT, EQUIPMENT

    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    @Column(nullable = false)
    private Integer requestedQuantity;

    @Column(name = "dispatched_quantity")
    private Integer dispatchedQuantity;

    @Column(name = "received_quantity")
    private Integer receivedQuantity;

    @Column(name = "condition_upon_receipt", length = 255)
    private String conditionUponReceipt;
}
