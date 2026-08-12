package com.healthcare.clinic.homevisit.repository;
import com.healthcare.clinic.homevisit.entity.HomeVisitRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface HomeVisitRequestRepository extends JpaRepository<HomeVisitRequest, Long> {
    List<HomeVisitRequest> findByPatientId(Long patientId);
}
