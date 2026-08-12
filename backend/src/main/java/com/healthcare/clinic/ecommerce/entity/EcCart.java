package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ec_carts")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(exclude = "items")
@ToString(exclude = "items")
public class EcCart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "session_key", unique = true, length = 128)
    private String sessionKey;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, MERGED, CHECKED_OUT, ABANDONED, EXPIRED

    @Column(name = "coupon_code", length = 100)
    private String couponCode;

    @Column(name = "loyalty_points_applied", nullable = false)
    @Builder.Default
    private Integer loyaltyPointsApplied = 0;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @Column(name = "merged_into_cart_id")
    private Long mergedIntoCartId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EcCartItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
