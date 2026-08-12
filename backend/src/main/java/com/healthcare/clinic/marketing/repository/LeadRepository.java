package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    Page<Lead> findByBranchIdAndStatus(Long branchId, String status, Pageable pageable);

    Page<Lead> findByOwnerIdAndStatus(Long ownerId, String status, Pageable pageable);

    Optional<Lead> findByDeduplicationKey(String deduplicationKey);

    List<Lead> findByStatusAndBranchId(String status, Long branchId);

    long countByBranchIdAndStatus(Long branchId, String status);
}
