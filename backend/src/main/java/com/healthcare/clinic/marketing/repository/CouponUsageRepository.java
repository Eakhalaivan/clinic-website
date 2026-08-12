package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    long countByCouponIdAndPatientId(Long couponId, Long patientId);
}
