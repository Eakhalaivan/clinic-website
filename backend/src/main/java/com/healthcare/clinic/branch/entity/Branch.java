package com.healthcare.clinic.branch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import com.healthcare.clinic.tenant.entity.Tenant;

@Entity
@Table(name = "branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Tenant tenant;

    @jakarta.validation.constraints.NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @jakarta.validation.constraints.NotBlank
    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String state;

    @jakarta.validation.constraints.NotBlank
    @Column(nullable = false, length = 50)
    private String country;

    @Column(nullable = false, length = 20)
    private String postalCode;

    @jakarta.validation.constraints.Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Column(length = 20)
    private String phoneNumber;

    @jakarta.validation.constraints.Email
    @Column(length = 100)
    private String email;

    @Builder.Default
    private Boolean isActive = true;

    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Pattern(regexp = "^([a-zA-Z_]+/[a-zA-Z_-]+|UTC)$", message = "Must be a valid IANA timezone")
    @Column(nullable = false, length = 50)
    private String timezone;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<OperatingHours> operatingHours = new ArrayList<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<StaffAssignment> staffAssignments = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    public void addOperatingHours(OperatingHours hours) {
        operatingHours.add(hours);
        hours.setBranch(this);
    }
}
