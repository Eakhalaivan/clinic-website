package com.healthcare.clinic.ecommerce.repository;

import com.healthcare.clinic.ecommerce.entity.EcPrescriptionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcPrescriptionLinkRepository extends JpaRepository<EcPrescriptionLink, Long> {
}
