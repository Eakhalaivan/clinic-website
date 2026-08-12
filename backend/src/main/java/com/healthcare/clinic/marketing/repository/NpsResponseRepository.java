package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.NpsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NpsResponseRepository extends JpaRepository<NpsResponse, Long> {
    Optional<NpsResponse> findBySurveyId(Long surveyId);

    @Query("SELECT AVG(r.npsScore) FROM NpsResponse r WHERE r.surveyId IN " +
           "(SELECT s.id FROM NpsSurvey s WHERE s.branchId = :branchId AND s.status = 'COMPLETED')")
    Double averageNpsScoreForBranch(@Param("branchId") Long branchId);
}
