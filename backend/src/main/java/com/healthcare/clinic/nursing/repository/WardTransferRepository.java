package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.WardTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardTransferRepository extends JpaRepository<WardTransfer, Long> {
    List<WardTransfer> findByPatientIdOrderByRequestedAtDesc(Long patientId);
    List<WardTransfer> findByDestinationBedIdAndStatus(Long destinationBedId, String status);
}
