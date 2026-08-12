package com.healthcare.clinic.laboratory.repository;

import com.healthcare.clinic.laboratory.entity.LabBarcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabBarcodeRepository extends JpaRepository<LabBarcode, Long> {
    Optional<LabBarcode> findByBarcodeValue(String barcodeValue);
    List<LabBarcode> findByLabRequestNumber(String labRequestNumber);
}
