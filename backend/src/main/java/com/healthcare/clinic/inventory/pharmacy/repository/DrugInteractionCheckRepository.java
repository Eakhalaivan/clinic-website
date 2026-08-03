package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.DrugInteractionCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyDrugInteractionCheckRepository")
public interface DrugInteractionCheckRepository extends JpaRepository<DrugInteractionCheck, String> {
}
