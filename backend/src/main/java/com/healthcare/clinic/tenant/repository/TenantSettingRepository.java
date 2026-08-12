package com.healthcare.clinic.tenant.repository;

import com.healthcare.clinic.tenant.entity.TenantSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantSettingRepository extends JpaRepository<TenantSetting, Long> {
}
