package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.Campaign;
import com.healthcare.clinic.marketing.entity.Coupon;
import com.healthcare.clinic.marketing.entity.PatientLoyalty;
import com.healthcare.clinic.marketing.entity.Referral;
import com.healthcare.clinic.marketing.repository.CampaignRepository;
import com.healthcare.clinic.marketing.repository.CouponRepository;
import com.healthcare.clinic.marketing.repository.PatientLoyaltyRepository;
import com.healthcare.clinic.marketing.repository.ReferralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarketingService {

    private final CampaignRepository campaignRepository;
    private final CouponRepository couponRepository;
    private final PatientLoyaltyRepository loyaltyRepository;
    private final ReferralRepository referralRepository;

    @Transactional(readOnly = true)
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Transactional
    public Campaign sendCampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
        campaign.setStatus("SENT");
        campaign.setSentCount(150); // Simulated dispatch count to audience
        campaign.setSentAt(ZonedDateTime.now());
        return campaignRepository.save(campaign);
    }

    @Transactional(readOnly = true)
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Transactional(readOnly = true)
    public Optional<Coupon> validateCoupon(String code) {
        return couponRepository.findByCodeAndIsActiveTrue(code);
    }

    @Transactional(readOnly = true)
    public PatientLoyalty getLoyalty(Long patientId) {
        return loyaltyRepository.findByPatientId(patientId)
                .orElseGet(() -> loyaltyRepository.save(PatientLoyalty.builder()
                        .patientId(patientId)
                        .pointsBalance(0)
                        .tier("BRONZE")
                        .build()));
    }

    @Transactional
    public PatientLoyalty addLoyaltyPoints(Long patientId, int points) {
        PatientLoyalty loyalty = getLoyalty(patientId);
        int newBalance = loyalty.getPointsBalance() + points;
        loyalty.setPointsBalance(newBalance);

        if (newBalance >= 1000) loyalty.setTier("PLATINUM");
        else if (newBalance >= 500) loyalty.setTier("GOLD");
        else if (newBalance >= 200) loyalty.setTier("SILVER");
        else loyalty.setTier("BRONZE");

        return loyaltyRepository.save(loyalty);
    }

    @Transactional(readOnly = true)
    public List<Referral> getAllReferrals() {
        return referralRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Referral createReferral(Referral referral) {
        return referralRepository.save(referral);
    }
}
