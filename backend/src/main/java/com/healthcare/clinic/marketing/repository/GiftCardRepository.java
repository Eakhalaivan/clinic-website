package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

    Optional<GiftCard> findByCodeHash(String codeHash);

    /** Pessimistic lock for concurrent redemption safety */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<GiftCard> findWithLockByCodeHash(String codeHash);
}
