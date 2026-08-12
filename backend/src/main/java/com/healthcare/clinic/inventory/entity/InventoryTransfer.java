package com.healthcare.clinic.inventory.entity;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventory_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class InventoryTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_branch_id", nullable = false)
    private Branch sourceBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_branch_id", nullable = false)
    private Branch destinationBranch;

    @Column(nullable = false, length = 50)
    private String status; // REQUESTED, APPROVED, IN_TRANSIT, RECEIVED, CANCELLED, REJECTED

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(length = 255)
    private String reason;

    @Column(name = "dispatched_at")
    private ZonedDateTime dispatchedAt;

    @Column(name = "received_at")
    private ZonedDateTime receivedAt;

    @OneToMany(mappedBy = "inventoryTransfer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryTransferItem> items = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    public void addItem(InventoryTransferItem item) {
        items.add(item);
        item.setInventoryTransfer(this);
    }
}
