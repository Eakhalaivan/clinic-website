package com.healthcare.clinic.pharmacy.entity;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pharmacy_ward_replacement_requests")
@SQLDelete(sql = "UPDATE pharmacy_ward_replacement_requests SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
public class WardReplacementRequest extends BaseEntity {

    @Column(name = "request_number", unique = true, nullable = false)
    private String requestNumber;

    @Column(nullable = false)
    private String ward;

    @Column(name = "requested_by", nullable = false)
    private String requestedBy;

    // PENDING, APPROVED, REJECTED
    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WardReplacementRequestItem> items = new ArrayList<>();

    // Getters and Setters
    public String getRequestNumber() { return requestNumber; }
    public void setRequestNumber(String requestNumber) { this.requestNumber = requestNumber; }
    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
    public List<WardReplacementRequestItem> getItems() { return items; }
    public void setItems(List<WardReplacementRequestItem> items) { this.items = items; }
}
