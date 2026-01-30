package com.iris.OnlineCompilerBackend.repositories;

import com.iris.OnlineCompilerBackend.models.CandidateMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateMasterRepo extends JpaRepository<CandidateMaster,Long> {
    Optional<CandidateMaster> findByCandidateId(String candidateId);

    @Query("""
            SELECT cm FROM CandidateMaster cm
            WHERE cm.candidateEmail=:candidateEmail
            """)
    CandidateMaster findByCandidateEmail(String candidateEmail);
}
