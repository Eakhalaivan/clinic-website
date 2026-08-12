package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.ShiftTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftTemplateRepository extends JpaRepository<ShiftTemplate, Long> {
    List<ShiftTemplate> findByBranchId(Long branchId);
}
