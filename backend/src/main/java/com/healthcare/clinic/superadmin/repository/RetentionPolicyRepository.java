package com.healthcare.clinic.superadmin.repository;
import com.healthcare.clinic.superadmin.entity.RetentionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RetentionPolicyRepository extends JpaRepository<RetentionPolicy, Long> {}
