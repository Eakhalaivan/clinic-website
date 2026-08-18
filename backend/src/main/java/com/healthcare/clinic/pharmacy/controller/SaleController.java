package com.healthcare.clinic.pharmacy.controller;


import com.healthcare.clinic.pharmacy.entity.*;
import com.healthcare.clinic.pharmacy.model.*;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.pharmacy.dto.SaleRequestDTO;
import com.healthcare.clinic.pharmacy.model.PharmacyBill;
import com.healthcare.clinic.pharmacy.service.SaleService;
import com.healthcare.clinic.pharmacy.repository.PharmacyBillRepository;
import com.healthcare.clinic.pharmacy.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.healthcare.clinic.pharmacy.service.BillPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController("pharmacySaleController")
@RequestMapping("/api/pharmacy/sales")
public class SaleController {

    private final SaleService saleService;
    private final PharmacyBillRepository pharmacyBillRepository;
    private final BillPdfService billPdfService;

    public SaleController(SaleService saleService, PharmacyBillRepository pharmacyBillRepository, BillPdfService billPdfService) {
        this.saleService = saleService;
        this.pharmacyBillRepository = pharmacyBillRepository;
        this.billPdfService = billPdfService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_BILLING_STAFF','ROLE_SUPERVISOR','ROLE_PHARMACIST')")
    @PostMapping
    public ResponseEntity<ApiResponse<PharmacyBill>> createSale(@Valid @RequestBody SaleRequestDTO request) {
        PharmacyBill bill = saleService.processSale(request);
        return ResponseEntity.ok(ApiResponse.success(bill, "Sale completed successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PharmacyBill>>> getAllSales(
            @PageableDefault(size = 20, sort = "billingDate", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<PharmacyBill> sales = pharmacyBillRepository.findAllWithItemsPaged(pageable);
            return ResponseEntity.ok(ApiResponse.success(sales, "Sales fetched"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("Error fetching sales: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PharmacyBill>> getSaleById(@PathVariable Long id) {
        PharmacyBill bill = pharmacyBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        return ResponseEntity.ok(ApiResponse.success(bill, "Bill details fetched"));
    }

    @GetMapping("/number/{billNumber}")
    public ResponseEntity<ApiResponse<PharmacyBill>> getSaleByBillNumber(@PathVariable String billNumber) {
        PharmacyBill bill = pharmacyBillRepository.findByBillNumber(billNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found: " + billNumber));
        return ResponseEntity.ok(ApiResponse.success(bill, "Bill fetched"));
    }

    @PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSale(@PathVariable Long id) {
        saleService.cancelSale(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Bill cancelled successfully"));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getSalePdf(@PathVariable Long id) {
        PharmacyBill bill = pharmacyBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        byte[] pdfBytes = billPdfService.generateBillPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "BILL-" + bill.getBillNumber() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
