package com.healthcare.clinic.pharmacy.model;

import com.healthcare.clinic.inventory.entity.BaseEntity;


import com.healthcare.clinic.pharmacy.entity.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@Table(name = "pharmacy_sales_bills")
@SQLDelete(sql = "UPDATE sales_bills SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@FilterDef(name = "branchFilter", parameters = @ParamDef(name = "branchId", type = Long.class))
@Filter(name = "branchFilter", condition = "branch_id = :branchId")
public class PharmacyBill extends BaseEntity {

    @Column(name = "bill_number", nullable = false, unique = true)
    private String billNumber;

    @Column(name = "branch_id", nullable = false)
    private Long branchId = 1L;

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    @Column(name = "patient_id")
    private Long patientId;

    private String patientName;
    private String doctorName;

    @Column(name = "doctor_id")
    private Long doctorId;

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    @Column(name = "bill_date", nullable = false)
    private LocalDateTime billingDate;

    @Column(name = "bill_type", nullable = false)
    private String billType = "CASH"; // CREDIT, CASH, OTC

    @Column(name = "sub_total", nullable = false)
    private BigDecimal subTotal;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "net_amount", nullable = false)
    private BigDecimal netAmount;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Column(name = "balance_amount")
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "bill_status", nullable = false)
    private String status = "PAID"; // PENDING, PAID, CANCELLED

    @Column(name = "amount_due")
    private BigDecimal amountDue = BigDecimal.ZERO;

    @Column(name = "advance_adjusted")
    private BigDecimal advanceAdjusted = BigDecimal.ZERO;

    @Column(name = "total_gst_amount")
    private BigDecimal totalGstAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private com.healthcare.clinic.pharmacy.enums.PaymentStatus paymentStatus;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    // Getters and Setters
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public LocalDateTime getBillingDate() { return billingDate; }
    public void setBillingDate(LocalDateTime billingDate) { this.billingDate = billingDate; }
    
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    
    public BigDecimal getSubTotal() { return subTotal; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }
    
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    
    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }
    
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    
    public BigDecimal getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public com.healthcare.clinic.pharmacy.enums.PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(com.healthcare.clinic.pharmacy.enums.PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    @JsonManagedReference
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PharmacyBillItem> items = new ArrayList<>();

    public List<PharmacyBillItem> getItems() { return items; }
    public void setItems(List<PharmacyBillItem> items) { this.items = items; }
}
