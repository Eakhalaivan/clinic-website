package com.healthcare.clinic.clinicaldecision.repository;

import com.healthcare.clinic.clinicaldecision.entity.OrderSetTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSetTemplateRepository extends JpaRepository<OrderSetTemplate, Long> {
    List<OrderSetTemplate> findByCategoryContainingIgnoreCase(String category);

    @Query(value = "SELECT * FROM order_set_templates WHERE diagnosis_codes @> jsonb_build_array(:code)", nativeQuery = true)
    List<OrderSetTemplate> findByDiagnosisCode(@Param("code") String code);
}
