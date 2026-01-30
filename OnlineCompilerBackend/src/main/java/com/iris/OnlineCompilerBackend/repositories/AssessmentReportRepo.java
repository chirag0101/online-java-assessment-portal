package com.iris.OnlineCompilerBackend.repositories;

import com.iris.OnlineCompilerBackend.dtos.AssessmentReportDTO;
import com.iris.OnlineCompilerBackend.models.AssessmentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentReportRepo extends JpaRepository<AssessmentReport, Long> {
    @Query("""
            SELECT new com.iris.OnlineCompilerBackend.dtos.AssessmentReportDTO(ar.langType,ar.score,ar.comments) FROM AssessmentReport ar
            LEFT JOIN Candidate c ON ar.candidateUserIdFk.userId = c.userId
            WHERE c.userId = :candidateId AND c.assessmentIsEnded=true
            ORDER BY ar.langType
            """)
    List<AssessmentReportDTO> findAssessmentsReportByCandidateId(Long candidateId);

    @Query("""
            FROM AssessmentReport ar
            LEFT JOIN Candidate c ON ar.candidateUserIdFk.userId = c.userId
            WHERE c.userId = :candidateId
            ORDER BY ar.langType
            """)
    List<AssessmentReport> findAssessmentReportByCandidateId(Long candidateId);

    @Query("""
            SELECT DISTINCT ar.langType FROM AssessmentReport ar
            LEFT JOIN Candidate c ON ar.candidateUserIdFk.userId = c.userId
            WHERE c.userId = :candidateId
            """)
    List<String> findAssessmentCodeTypesByUserId(Long candidateId);
}
