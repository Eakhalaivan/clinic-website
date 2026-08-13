package com.healthcare.clinic.pharmacy.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pharmacy_ward_replacement_return_items")
public class WardReplacementReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    private WardReplacementReturn returnRequest;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "returned_qty", nullable = false)
    private Integer returnedQty;

    // Getters and Setters
    public Long getId() { return id; }
    public WardReplacementReturn getReturnRequest() { return returnRequest; }
    public void setReturnRequest(WardReplacementReturn returnRequest) { this.returnRequest = returnRequest; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public Integer getReturnedQty() { return returnedQty; }
    public void setReturnedQty(Integer returnedQty) { this.returnedQty = returnedQty; }
}
