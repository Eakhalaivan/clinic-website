package com.healthcare.clinic.inventory.sales.repository;

import com.healthcare.clinic.inventory.sales.model.BillCancellationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyBillCancellationRequestRepository")
public interface BillCancellationRequestRepository extends JpaRepository<BillCancellationRequest, Long> {
}
