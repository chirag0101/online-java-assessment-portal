package com.iris.OnlineCompilerBackend.repositories;

import com.iris.OnlineCompilerBackend.dtos.response.ActiveAssessmentsResDTO;
import com.iris.OnlineCompilerBackend.dtos.response.CandidateDetailsResDTO;
import com.iris.OnlineCompilerBackend.dtos.response.SubmissionsResDTO;
import com.iris.OnlineCompilerBackend.dtos.response.ViewCandidatesStatusResDTO;
import com.iris.OnlineCompilerBackend.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate, Long> {

    @Query(
            """
                    FROM Candidate c WHERE c.candidateIdCode=:candidateId AND c.urlIsActive=true
                    """)
    List<Candidate> findAllActiveAssessmentsForCandidate(@Param("candidateId") String candidateId);

    @Query("FROM Candidate c " +
            "WHERE c.candidateIdCode=:candidateId " +
            "AND c.urlIsActive=true " +
            "AND c.interviewRound=:interviewRound")
    Optional<Candidate> findByCandidateIdAndAssessmentIsActiveAndAssessmentRound(@Param("candidateId") String candidateId, @Param("interviewRound") String interviewRound);

    @Query("""
            FROM Candidate c
            WHERE c.candidateIdCode=:candidateId
            ORDER BY c.urlCreatedTime DESC FETCH FIRST 1 ROWS ONLY
            """)
    Optional<Candidate> findByCandidateIdLatestEntry(@Param("candidateId") String candidateId);

    @Query("FROM Candidate c " +
            "WHERE c.candidateIdCode=:candidateId " +
            "AND c.assessmentIsStarted=true " +
            "AND c.urlIsActive=true ")
    Optional<Candidate> findByCandidateIdAndAssessmentIsActive(@Param("candidateId") String candidateId);

    @Query("FROM Candidate c " +
            "WHERE c.candidateIdCode=:candidateId " +
            "AND c.urlHashCode=:urlHashCode ")
    Candidate findByCandidateIdAndUrl(@Param("candidateId") String candidateId, @Param("urlHashCode") String urlHashCode);

    @Query("""
            FROM Candidate c
            WHERE c.candidateIdCode=:candidateId
            AND c.interviewerId=:interviewerId
            AND c.assessmentEndTime=:assessmentEndTime
            AND c.interviewRound=:round
            AND c.assessmentIsEnded=true
            """)
    Optional<Candidate> findByCandidateDetails(@Param("candidateId") String candidateId, @Param("interviewerId") String interviewerId, @Param("assessmentEndTime") Date assessmentEndTime, @Param("round") String round);

    @Query("FROM Candidate c " +
            "WHERE c.candidateIdCode=:candidateId AND c.urlIsActive=true AND c.assessmentIsStarted=true")
    Optional<Candidate> findByCandidateIdAndIsActive(@Param("candidateId") String candidateId);

    @Query("FROM Candidate c " +
            "WHERE c.candidateIdCode=:candidateId AND c.urlIsActive=true AND c.assessmentIsStarted=true AND c.interviewRound=:round")
    Optional<Candidate> findByCandidateIdIsActiveAndRound(@Param("candidateId") String candidateId, @Param("round") String round);

    @Query("FROM Candidate c " +
            "WHERE c.candidateIdCode=:candidateId AND c.urlIsActive=true AND c.assessmentIsStarted=true AND c.urlHashCode=:url")
    Optional<Candidate> findByCandidateIdByUrlAndIsActive(@Param("candidateId") String candidateId, @Param("url") String url);

    @Query("SELECT c.userId " +
            "FROM Candidate c " +
            "WHERE c.candidateIdCode=:candidateId")
    Long findUserIdByCandidateId(@Param("candidateId") String candidateId);

    @Query("""
            SELECT new com.iris.OnlineCompilerBackend.dtos.response.ActiveAssessmentsResDTO(c.candidateIdCode,c.interviewerId,cm.candidateName,c.yearsOfExperience,c.technology,c.urlExpiryTime,c.assessmentStartTime,c.assessmentEndTime)
            FROM Candidate c
            LEFT JOIN CandidateMaster cm ON cm.userId=c.candidateIdFk.userId
            WHERE c.urlIsActive=true
            """)
    List<ActiveAssessmentsResDTO> findAllActiveAssessments();

    @Query("FROM Candidate c WHERE c.urlIsActive=true")
    List<Candidate> findAllActiveAssessmentCandidates();

    @Query("""
            SELECT new com.iris.OnlineCompilerBackend.dtos.response.CandidateDetailsResDTO(c.candidateIdCode,cm.candidateName,c.technology,c.yearsOfExperience,cm.candidateEmail,c.interviewRound,c.assessmentDateTime)
            FROM Candidate c
            LEFT JOIN CandidateMaster cm ON cm.userId=c.candidateIdFk.userId
            WHERE c.urlIsActive=true AND c.urlHashCode=:hashCode
            """)
    CandidateDetailsResDTO findDetailsByUrl(@Param("hashCode") String hashCode);

    @Query("""
            SELECT new com.iris.OnlineCompilerBackend.dtos.response.CandidateDetailsResDTO(c.candidateIdCode,cm.candidateName,c.technology,c.yearsOfExperience,cm.candidateEmail,c.interviewRound)
            FROM CandidateMaster cm
            LEFT JOIN Candidate c ON cm.userId=c.candidateIdFk.userId
            WHERE c.urlExpiryTime =
            (SELECT MAX(c2.urlExpiryTime)
            FROM Candidate c2
            WHERE c2.candidateIdCode=c.candidateIdCode)
            ORDER BY cm.candidateName
            """)
    List<CandidateDetailsResDTO> findAllCandidates();

    @Query("""
            SELECT new com.iris.OnlineCompilerBackend.dtos.response.SubmissionsResDTO(c.candidateIdCode,cm.candidateName,c.interviewerId,s.time,s.code,s.output,s.action,s.status) FROM CodeSnippet s
            LEFT JOIN Candidate c ON c.userId=s.userIdFk.userId
            LEFT JOIN CandidateMaster cm ON cm.userId=c.candidateIdFk.userId
            ORDER BY s.time DESC
            """)
    List<SubmissionsResDTO> findAllSubmissions();

    @Query("""
            SELECT new com.iris.OnlineCompilerBackend.dtos.response.SubmissionsResDTO(c.candidateIdCode,cm.candidateName,c.interviewerId,s.time,s.code,s.output,s.action,s.status)
            FROM CodeSnippet s
            LEFT JOIN Candidate c ON c.userId=s.userIdFk.userId
            LEFT JOIN CandidateMaster cm ON cm.userId=c.candidateIdFk.userId
            WHERE c.interviewerId=:interviewerId
            """)
    List<SubmissionsResDTO> findAllSubmissionsByInterviewerId(@Param("interviewerId") String interviewerId);

    @Query("""
            SELECT new com.iris.OnlineCompilerBackend.dtos.response.CandidateDetailsResDTO(c.candidateIdCode,cm.candidateName,c.technology,c.yearsOfExperience,cm.candidateEmail,c.interviewRound)
            FROM Candidate c
            LEFT JOIN CandidateMaster cm ON cm.userId=c.candidateIdFk.userId
            WHERE cm.candidateId =:candidateId
            ORDER BY c.urlExpiryTime DESC
            FETCH FIRST 1 ROWS ONLY
            """)
    CandidateDetailsResDTO findByCandidateIdAndLatest(@Param("candidateId") String candidateId);

    @Query("""
            FROM Candidate c
            WHERE c.urlIsActive=true
            ORDER BY c.assessmentDateTime DESC
            """)
    List<Candidate> findAllActiveCandidateDetails();

    @Query("""
            FROM Candidate c
            WHERE c.interviewerId=:adminId
            AND
            c.urlIsActive=true
            AND
            c.assessmentIsStarted=true
            """)
    List<Candidate> findAllActiveAssessmentsByAdminId(@Param("adminId") String adminId);

    @Query("""
            SELECT c.url
            FROM Candidate c
            WHERE c.candidateIdCode=:candidateId
            AND
            c.urlIsActive=true
            AND
            c.assessmentIsStarted=true
            """)
    String findActiveUrlById(@Param("candidateId") String candidateId);

    @Query("""
            SELECT c.assessmentEndTime FROM Candidate c
            WHERE c.candidateIdCode=:candidateId
            AND c.interviewerId=:interviewerId
            AND c.interviewRound=:round
            AND c.urlIsActive=true
            AND c.assessmentIsStarted=true
            """)
    Date findAssessmentEndTimeByCandId(@Param("candidateId") String candidateId, @Param("interviewerId") String interviewerId, @Param("round") String round);

    @Query("""
               SELECT new com.iris.OnlineCompilerBackend.dtos.response.ViewCandidatesStatusResDTO(c.candidateIdCode,c.candidateIdFk.candidateName, c.interviewerId, c.technology, c.interviewRound, c.assessmentEndTime, c.status)
               FROM Candidate c
               WHERE c.assessmentIsEnded=true
               AND c.interviewerId=:interviewerId
               ORDER BY c.assessmentEndTime DESC
            """)
    List<ViewCandidatesStatusResDTO> getCandidatesHistory(@Param("interviewerId") String interviewerId);

    @Query("""
               SELECT new com.iris.OnlineCompilerBackend.dtos.response.ViewCandidatesStatusResDTO(c.candidateIdCode,c.candidateIdFk.candidateName, c.interviewerId, c.technology, c.interviewRound, c.assessmentEndTime, c.status)
               FROM Candidate c
               WHERE c.assessmentIsEnded=true
               ORDER BY c.assessmentEndTime DESC
            """)
    List<ViewCandidatesStatusResDTO> getCandidatesHistoryForMasterAdmin();

    @Query("""
            SELECT c.isReviewed FROM Candidate c
            WHERE c.candidateIdCode=:candidateId
            AND c.interviewerId=:interviewerId
            AND c.assessmentEndTime=:assessmentEndTime
            AND c.interviewRound=:round
            AND c.assessmentIsEnded=true
            """)
    Boolean getIsAssessmentIsReviewed(@Param("candidateId") String candidateId, @Param("interviewerId") String interviewerId, @Param("assessmentEndTime") Date assessmentEndTime, @Param("round") String round);
}
