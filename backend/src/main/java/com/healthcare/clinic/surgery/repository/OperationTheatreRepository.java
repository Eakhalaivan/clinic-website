package com.healthcare.clinic.surgery.repository;

import com.healthcare.clinic.surgery.entity.OperationTheatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationTheatreRepository extends JpaRepository<OperationTheatre, Long> {
    List<OperationTheatre> findByBranchId(Long branchId);
}
