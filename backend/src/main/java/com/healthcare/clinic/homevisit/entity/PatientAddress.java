package com.healthcare.clinic.homevisit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import java.math.BigDecimal;

@Entity
@Table(name = "patient_addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class PatientAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false)
    private Long patientId;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String landmark;
    
    private boolean isDefault;

    @Column(precision = 10, scale = 6)
    private BigDecimal latitude;
    
    @Column(precision = 10, scale = 6)
    private BigDecimal longitude;
}
