package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
    List<Referral> findAllByOrderByCreatedAtDesc();
    List<Referral> findByReferrerId(Long referrerId);
    Optional<Referral> findByReferralCode(String referralCode);
    long countByReferrerIdAndStatus(Long referrerId, String status);
    long countByStatus(String status);
    boolean existsByReferrerIdAndRefereeEmail(Long referrerId, String refereeEmail);
}
