package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.DrugInteractionCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyDrugInteractionCheckRepository")
public interface DrugInteractionCheckRepository extends JpaRepository<DrugInteractionCheck, String> {
}
