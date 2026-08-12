package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> findByWardIdAndIsActiveTrue(Long wardId);
    Optional<Bed> findByWardIdAndBedNumber(Long wardId, String bedNumber);
}
