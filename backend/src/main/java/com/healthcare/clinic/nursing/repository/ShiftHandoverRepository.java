package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.ShiftHandover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftHandoverRepository extends JpaRepository<ShiftHandover, Long> {
    List<ShiftHandover> findByWardIdOrderByHandoverTimeDesc(Long wardId);
    List<ShiftHandover> findByIncomingNurseIdOrderByHandoverTimeDesc(Long nurseId);
    List<ShiftHandover> findByOutgoingNurseIdOrderByHandoverTimeDesc(Long nurseId);
}
