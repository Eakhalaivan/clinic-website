package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyMedicineRepository")
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByNameContainingIgnoreCase(String name);
    java.util.Optional<Medicine> findByBarcode(String barcode);
    @org.springframework.data.jpa.repository.Query("SELECT m FROM Medicine m WHERE " +
       "(:search IS NULL OR :search = '' OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(m.genericName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(m.medicineCode) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
       "(:drugClass IS NULL OR :drugClass = '' OR :drugClass = 'ALL' OR m.drugClass = :drugClass) AND " +
       "(:schedule IS NULL OR :schedule = '' OR :schedule = 'ALL' OR m.schedule = :schedule) AND " +
       "(:productType IS NULL OR :productType = '' OR :productType = 'ALL' OR m.productType = :productType)")
    org.springframework.data.domain.Page<Medicine> searchMedicines(
        @org.springframework.data.repository.query.Param("search") String search, 
        @org.springframework.data.repository.query.Param("drugClass") String drugClass, 
        @org.springframework.data.repository.query.Param("schedule") String schedule, 
        @org.springframework.data.repository.query.Param("productType") String productType, 
        org.springframework.data.domain.Pageable pageable);

}
