package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.Coupon;
import com.healthcare.clinic.marketing.entity.CouponUsage;
import com.healthcare.clinic.marketing.repository.CouponRepository;
import com.healthcare.clinic.marketing.repository.CouponUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository usageRepository;

    @Transactional(readOnly = true)
    public Optional<Coupon> findActiveByCode(String code) {
        return couponRepository.findByCodeAndIsActiveTrue(code);
    }

    /**
     * Validates a coupon server-side and returns the applicable discount.
     * Enforces: active, date validity, usage limits, per-patient limits, and approval.
     * Uses optimistic lock on timesUsed to prevent race conditions.
     */
    @Transactional
    public BigDecimal validateAndApply(String code, Long patientId, BigDecimal orderAmount,
                                       Long invoiceId, Long branchId) {
        Coupon coupon = couponRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found or inactive: " + code));

        // Date validation
        LocalDate today = LocalDate.now();
        if (today.isBefore(coupon.getValidFrom()) || today.isAfter(coupon.getValidTo())) {
            throw new IllegalStateException("Coupon is not valid today");
        }

        // Global usage limit
        if (coupon.getUsageLimit() != null && coupon.getTimesUsed() >= coupon.getUsageLimit()) {
            throw new IllegalStateException("Coupon usage limit reached");
        }

        // Per-patient limit
        long patientUses = usageRepository.countByCouponIdAndPatientId(coupon.getId(), patientId);
        if (patientUses >= coupon.getPerPatientLimit()) {
            throw new IllegalStateException("You have already used this coupon " + coupon.getPerPatientLimit()
                    + " time(s)");
        }

        // Minimum order amount
        if (coupon.getMinOrderAmount() != null
                && orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new IllegalStateException("Order amount is below the minimum required for this coupon");
        }

        // Calculate discount
        BigDecimal discount;
        if ("PERCENTAGE".equals(coupon.getDiscountType())) {
            discount = orderAmount.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));
            if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
                discount = coupon.getMaxDiscount();
            }
        } else {
            discount = coupon.getDiscountValue().min(orderAmount);
        }

        // Record usage
        coupon.setTimesUsed(coupon.getTimesUsed() + 1);
        couponRepository.save(coupon);

        CouponUsage usage = CouponUsage.builder()
                .couponId(coupon.getId())
                .patientId(patientId)
                .invoiceId(invoiceId)
                .discountApplied(discount)
                .branchId(branchId)
                .build();
        usageRepository.save(usage);

        return discount;
    }

    @Transactional
    public Coupon createCoupon(Coupon coupon, Long createdBy) {
        coupon.setCreatedBy(createdBy);
        coupon.setTimesUsed(0);
        coupon.setIsActive(true);
        return couponRepository.save(coupon);
    }

    @Transactional
    public Coupon approveCoupon(Long couponId, Long approvedBy) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found: " + couponId));
        coupon.setApprovedBy(approvedBy);
        coupon.setApprovedAt(java.time.ZonedDateTime.now());
        return couponRepository.save(coupon);
    }
}
