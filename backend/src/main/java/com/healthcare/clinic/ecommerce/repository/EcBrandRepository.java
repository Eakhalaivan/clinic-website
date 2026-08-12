package com.healthcare.clinic.ecommerce.repository;

import com.healthcare.clinic.ecommerce.entity.EcBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcBrandRepository extends JpaRepository<EcBrand, Long> {
}
