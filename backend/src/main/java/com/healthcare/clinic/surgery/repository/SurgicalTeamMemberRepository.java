package com.healthcare.clinic.surgery.repository;

import com.healthcare.clinic.surgery.entity.SurgicalTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurgicalTeamMemberRepository extends JpaRepository<SurgicalTeamMember, Long> {
    List<SurgicalTeamMember> findBySurgeryBookingId(Long surgeryBookingId);
}
