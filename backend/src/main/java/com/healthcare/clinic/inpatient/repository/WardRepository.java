package com.healthcare.clinic.inpatient.repository;

import com.healthcare.clinic.inpatient.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByBranchId(Long branchId);
}
