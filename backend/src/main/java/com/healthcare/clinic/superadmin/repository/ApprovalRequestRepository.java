package com.healthcare.clinic.superadmin.repository;
import com.healthcare.clinic.superadmin.entity.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {}
