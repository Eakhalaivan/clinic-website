package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ec_return_items")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcReturnItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    private EcReturn ecReturn;

    @Column(name = "order_item_id", nullable = false)
    private Long orderItemId;

    @Column(name = "qty_returned", nullable = false)
    private Integer qtyReturned;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String disposition = "QUARANTINE"; // RESTOCK, QUARANTINE, DISPOSE

    @Column(name = "disposition_note", length = 300)
    private String dispositionNote;
}
