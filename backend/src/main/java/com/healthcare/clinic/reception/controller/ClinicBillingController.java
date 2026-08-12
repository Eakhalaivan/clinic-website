package com.healthcare.clinic.reception.controller;

import com.healthcare.clinic.reception.entity.ClinicBill;
import com.healthcare.clinic.reception.entity.ClinicPayment;
import com.healthcare.clinic.reception.service.ClinicBillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reception/billing")
@RequiredArgsConstructor
public class ClinicBillingController {

    private final ClinicBillingService billingService;

    @PostMapping("/bills")
    @PreAuthorize("hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClinicBill> createBill(@RequestBody Map<String, Object> request) {
        Long patientId = request.containsKey("patientId") && request.get("patientId") != null 
                ? Long.valueOf(request.get("patientId").toString()) 
                : null;
        Long appointmentId = request.containsKey("appointmentId") && request.get("appointmentId") != null 
                ? Long.valueOf(request.get("appointmentId").toString()) 
                : null;
        Long walkInId = request.containsKey("walkInId") && request.get("walkInId") != null 
                ? Long.valueOf(request.get("walkInId").toString()) 
                : null;
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");

        ClinicBill bill = billingService.createBill(patientId, appointmentId, walkInId, items);
        return ResponseEntity.ok(bill);
    }

    @PostMapping("/bills/{billId}/payments")
    @PreAuthorize("hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClinicPayment> recordPayment(
            @PathVariable Long billId,
            @RequestBody Map<String, Object> request) {
        
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String paymentMethod = (String) request.get("paymentMethod");
        String referenceNumber = (String) request.get("referenceNumber");

        ClinicPayment payment = billingService.recordPayment(billId, amount, paymentMethod, referenceNumber);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/patient/{patientId}/bills")
    @PreAuthorize("hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<ClinicBill>> getBillsForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(billingService.getBillsForPatient(patientId));
    }
}
