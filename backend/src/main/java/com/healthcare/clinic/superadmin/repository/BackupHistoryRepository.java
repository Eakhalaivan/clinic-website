package com.healthcare.clinic.superadmin.repository;
import com.healthcare.clinic.superadmin.entity.BackupHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {}
