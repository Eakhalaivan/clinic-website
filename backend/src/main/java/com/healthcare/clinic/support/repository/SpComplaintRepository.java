package com.healthcare.clinic.support.repository;

import com.healthcare.clinic.support.entity.SpComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpComplaintRepository extends JpaRepository<SpComplaint, Long> {
}
