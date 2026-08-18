package com.healthcare.clinic.inpatient.repository;

import com.healthcare.clinic.inpatient.entity.Bed;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    
    @Query("SELECT b FROM InpatientBed b JOIN FETCH b.room r JOIN FETCH r.ward w WHERE w.branchId = :branchId")
    List<Bed> findByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT b FROM InpatientBed b JOIN FETCH b.room r JOIN FETCH r.ward w WHERE b.status = :status AND w.branchId = :branchId")
    List<Bed> findByStatusAndBranchId(@Param("status") String status, @Param("branchId") Long branchId);

    @Lock(LockModeType.OPTIMISTIC)
    @EntityGraph(attributePaths = {"room", "room.ward"})
    @Query("SELECT b FROM InpatientBed b WHERE b.id = :id")
    Optional<Bed> findByIdWithLock(@Param("id") Long id);
}
