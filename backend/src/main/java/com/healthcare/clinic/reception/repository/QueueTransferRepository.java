package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.QueueTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueueTransferRepository extends JpaRepository<QueueTransfer, Long> {
    List<QueueTransfer> findByTokenId(Long tokenId);
}
