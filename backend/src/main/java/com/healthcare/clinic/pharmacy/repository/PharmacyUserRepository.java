package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.pharmacy.entity.PharmacyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pharmacyUserRepository")
public interface PharmacyUserRepository extends JpaRepository<PharmacyUser, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"roles"})
    java.util.Optional<PharmacyUser> findByUsername(String username);
    
    long countByStatus(String status);
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"roles"})
    java.util.List<PharmacyUser> findByStatus(String status);

    java.util.List<PharmacyUser> findAllByDeletedFalse();

    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE PharmacyUser u SET u.lastLogin = :lastLogin WHERE u.id = :id")
    void updateLastLogin(@org.springframework.data.repository.query.Param("id") Long id, @org.springframework.data.repository.query.Param("lastLogin") java.time.LocalDateTime lastLogin);
}
