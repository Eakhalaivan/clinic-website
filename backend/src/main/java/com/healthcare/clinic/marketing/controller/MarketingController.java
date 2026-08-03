package com.healthcare.clinic.marketing.controller;

import com.healthcare.clinic.marketing.entity.Campaign;
import com.healthcare.clinic.marketing.entity.Coupon;
import com.healthcare.clinic.marketing.entity.PatientLoyalty;
import com.healthcare.clinic.marketing.entity.Referral;
import com.healthcare.clinic.marketing.service.MarketingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketing")
@RequiredArgsConstructor
public class MarketingController {

    private final MarketingService marketingService;

    @GetMapping("/campaigns")
    @PreAuthorize("hasRole('MARKETING') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Campaign>> getCampaigns() {
        return ResponseEntity.ok(marketingService.getAllCampaigns());
    }

    @PostMapping("/campaigns")
    @PreAuthorize("hasRole('MARKETING') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        return ResponseEntity.ok(marketingService.createCampaign(campaign));
    }

    @PostMapping("/campaigns/{id}/send")
    @PreAuthorize("hasRole('MARKETING') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Campaign> sendCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(marketingService.sendCampaign(id));
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<Coupon>> getCoupons() {
        return ResponseEntity.ok(marketingService.getAllCoupons());
    }

    @PostMapping("/coupons")
    @PreAuthorize("hasRole('MARKETING') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(marketingService.createCoupon(coupon));
    }

    @GetMapping("/coupons/validate/{code}")
    public ResponseEntity<Coupon> validateCoupon(@PathVariable String code) {
        return marketingService.validateCoupon(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/loyalty/{patientId}")
    public ResponseEntity<PatientLoyalty> getLoyalty(@PathVariable Long patientId) {
        return ResponseEntity.ok(marketingService.getLoyalty(patientId));
    }

    @PostMapping("/loyalty/add")
    @PreAuthorize("hasRole('MARKETING') or hasRole('SUPER_ADMIN') or hasRole('RECEPTION')")
    public ResponseEntity<PatientLoyalty> addLoyaltyPoints(
            @RequestParam Long patientId,
            @RequestParam int points) {
        return ResponseEntity.ok(marketingService.addLoyaltyPoints(patientId, points));
    }

    @GetMapping("/referrals")
    @PreAuthorize("hasRole('MARKETING') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Referral>> getReferrals() {
        return ResponseEntity.ok(marketingService.getAllReferrals());
    }

    @PostMapping("/referrals")
    public ResponseEntity<Referral> createReferral(@RequestBody Referral referral) {
        return ResponseEntity.ok(marketingService.createReferral(referral));
    }
}
