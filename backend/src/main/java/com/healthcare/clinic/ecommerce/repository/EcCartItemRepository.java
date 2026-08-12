package com.healthcare.clinic.ecommerce.repository;

import com.healthcare.clinic.ecommerce.entity.EcCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcCartItemRepository extends JpaRepository<EcCartItem, Long> {
}
