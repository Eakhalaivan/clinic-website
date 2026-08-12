package com.healthcare.clinic.pharmacy.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pharmacy_ward_replacement_request_items")
public class WardReplacementRequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private WardReplacementRequest request;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "requested_qty", nullable = false)
    private Integer requestedQty;

    @Column(name = "available_stock_at_request")
    private Integer availableStockAtRequest;

    // Getters and Setters
    public Long getId() { return id; }
    public WardReplacementRequest getRequest() { return request; }
    public void setRequest(WardReplacementRequest request) { this.request = request; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public Integer getRequestedQty() { return requestedQty; }
    public void setRequestedQty(Integer requestedQty) { this.requestedQty = requestedQty; }
    public Integer getAvailableStockAtRequest() { return availableStockAtRequest; }
    public void setAvailableStockAtRequest(Integer availableStockAtRequest) { this.availableStockAtRequest = availableStockAtRequest; }
}
