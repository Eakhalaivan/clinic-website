package com.healthcare.clinic.superadmin.repository;
import com.healthcare.clinic.superadmin.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {}
