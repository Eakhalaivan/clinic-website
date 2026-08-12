package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.model.BillCancellationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyBillCancellationRequestRepository")
public interface BillCancellationRequestRepository extends JpaRepository<BillCancellationRequest, Long> {
}
