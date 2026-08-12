package com.healthcare.clinic.homevisit.repository;
import com.healthcare.clinic.homevisit.entity.HomeVisitAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface HomeVisitAssignmentRepository extends JpaRepository<HomeVisitAssignment, Long> {
    List<HomeVisitAssignment> findByStaffUserId(Long staffUserId);
}
