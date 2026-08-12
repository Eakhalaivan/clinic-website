package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_brands")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcBrand {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(length = 300)
    private String manufacturer;

    @Column(name = "country_of_origin", length = 100)
    private String countryOfOrigin;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "compliance_status", nullable = false, length = 30)
    @Builder.Default
    private String complianceStatus = "COMPLIANT"; // COMPLIANT, UNDER_REVIEW, SUSPENDED

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
