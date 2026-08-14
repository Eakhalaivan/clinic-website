package com.healthcare.clinic.marketing.controller;

import com.healthcare.clinic.marketing.entity.MarketingCampaign;
import com.healthcare.clinic.marketing.repository.MarketingCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/marketing/legacy-campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final MarketingCampaignRepository campaignRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_MARKETING')")
    public ResponseEntity<List<MarketingCampaign>> getAllCampaigns() {
        return ResponseEntity.ok(campaignRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_MARKETING')")
    public ResponseEntity<MarketingCampaign> createCampaign(@RequestBody MarketingCampaign campaign) {
        if (campaign.getStatus() == null) {
            campaign.setStatus("DRAFT");
        }
        return ResponseEntity.ok(campaignRepository.save(campaign));
    }

    @PostMapping("/{campaignId}/send")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_MARKETING')")
    public ResponseEntity<MarketingCampaign> sendCampaign(@PathVariable Long campaignId) {
        MarketingCampaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));

        if (!"DRAFT".equals(campaign.getStatus())) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only DRAFT campaigns can be sent");
        }

        campaign.setStatus("SENT");
        return ResponseEntity.ok(campaignRepository.save(campaign));
    }
}
