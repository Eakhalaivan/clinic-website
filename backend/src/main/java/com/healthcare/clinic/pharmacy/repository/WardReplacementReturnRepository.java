package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.pharmacy.entity.WardReplacementReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardReplacementReturnRepository extends JpaRepository<WardReplacementReturn, Long> {
    List<WardReplacementReturn> findByStatusOrderByReturnDateDesc(String status);
    List<WardReplacementReturn> findAllByOrderByReturnDateDesc();
}
