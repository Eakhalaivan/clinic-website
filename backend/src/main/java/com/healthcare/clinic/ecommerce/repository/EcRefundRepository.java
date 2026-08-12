package com.healthcare.clinic.ecommerce.repository;

import com.healthcare.clinic.ecommerce.entity.EcRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcRefundRepository extends JpaRepository<EcRefund, Long> {
}
