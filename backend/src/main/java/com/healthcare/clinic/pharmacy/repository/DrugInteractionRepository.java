package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.DrugInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyDrugInteractionRepository")
public interface DrugInteractionRepository extends JpaRepository<DrugInteraction, String> {

    @Query("SELECT di FROM DrugInteraction di WHERE ((di.medicineA.id IN :ids AND di.medicineB.id IN :ids)) AND di.active = true")
    List<DrugInteraction> checkInteractions(@Param("ids") List<Long> ids);
}
