package com.healthcare.clinic.pharmacy.entity;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pharmacy_ward_replacement_returns")
@SQLDelete(sql = "UPDATE pharmacy_ward_replacement_returns SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
public class WardReplacementReturn extends BaseEntity {

    @Column(name = "return_number", unique = true, nullable = false)
    private String returnNumber;

    @Column(name = "request_number", nullable = false)
    private String requestNumber;

    @Column(nullable = false)
    private String ward;

    @Column(name = "returned_by", nullable = false)
    private String returnedBy;

    // PENDING, COMPLETED, REJECTED
    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "return_date", nullable = false)
    private LocalDateTime returnDate = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "returnRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WardReplacementReturnItem> items = new ArrayList<>();

    // Getters and Setters
    public String getReturnNumber() { return returnNumber; }
    public void setReturnNumber(String returnNumber) { this.returnNumber = returnNumber; }
    public String getRequestNumber() { return requestNumber; }
    public void setRequestNumber(String requestNumber) { this.requestNumber = requestNumber; }
    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }
    public String getReturnedBy() { return returnedBy; }
    public void setReturnedBy(String returnedBy) { this.returnedBy = returnedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public List<WardReplacementReturnItem> getItems() { return items; }
    public void setItems(List<WardReplacementReturnItem> items) { this.items = items; }
}
