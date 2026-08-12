package com.healthcare.clinic.ecommerce.repository;

import com.healthcare.clinic.ecommerce.entity.EcReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcReviewResponseRepository extends JpaRepository<EcReviewResponse, Long> {
}
