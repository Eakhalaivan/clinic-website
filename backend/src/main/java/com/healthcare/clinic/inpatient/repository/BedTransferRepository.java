package com.healthcare.clinic.inpatient.repository;

import com.healthcare.clinic.inpatient.entity.BedTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedTransferRepository extends JpaRepository<BedTransfer, Long> {
    List<BedTransfer> findByAdmissionId(Long admissionId);
}
