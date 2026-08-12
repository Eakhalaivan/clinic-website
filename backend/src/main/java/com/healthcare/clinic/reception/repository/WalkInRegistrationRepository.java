package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.WalkInRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalkInRegistrationRepository extends JpaRepository<WalkInRegistration, Long> {
    List<WalkInRegistration> findByBranchIdAndStatus(Long branchId, String status);
    List<WalkInRegistration> findByBranchId(Long branchId);
}
