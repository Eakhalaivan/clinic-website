package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByBranchIdAndIsActiveTrue(Long branchId);
}
