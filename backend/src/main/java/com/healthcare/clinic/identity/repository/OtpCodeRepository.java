package com.healthcare.clinic.identity.repository;

import com.healthcare.clinic.identity.entity.OtpCode;
import com.healthcare.clinic.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findByCode(String code);

    /** Used by OtpService rate-limiter to count recently issued codes for a user. */
    long countByUserAndExpiryDateAfterAndUsedFalse(User user, ZonedDateTime after);
}
