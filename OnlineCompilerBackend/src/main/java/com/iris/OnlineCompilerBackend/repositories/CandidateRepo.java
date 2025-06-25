package com.iris.OnlineCompilerBackend.repositories;

import com.iris.OnlineCompilerBackend.dtos.ActiveAssessmentsResDTO;
import com.iris.OnlineCompilerBackend.dtos.CandidateDetailsResDTO;
import com.iris.OnlineCompilerBackend.dtos.SubmissionsResDTO;
import com.iris.OnlineCompilerBackend.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate, Long> {
    @Query("FROM Candidate c " +
            "WHERE c.candidateId=:candidateId AND c.urlIsActive=true " +
            "ORDER BY c.assessmentEndTime")
    Optional<Candidate> findByCandidateIdLatestEntry(@Param("candidateId") String candidateId);

    @Query("FROM Candidate c " +
            "WHERE c.candidateId=:candidateId AND c.urlIsActive=true")
    Optional<Candidate> findByCandidateIdAndIsActive(@Param("candidateId") String candidateId);

    @Query("SELECT c.userId " +
            "FROM Candidate c " +
            "WHERE c.candidateId=:candidateId")
    Long findUserIdByCandidateId(@Param("candidateId") String candidateId);

    @Query("SELECT new com.iris.OnlineCompilerBackend.dtos.ActiveAssessmentsResDTO(c.candidateId,c.interviewerId,c.candidateFullName,c.yearsOfExperience,c.technology,c.urlExpiryTime,c.assessmentStartTime,c.assessmentEndTime)" +
            "FROM Candidate c " +
            "WHERE c.urlIsActive=true")
    List<ActiveAssessmentsResDTO> findAllActiveAssessments();

    @Query("FROM Candidate c WHERE c.urlIsActive=true AND c.assessmentIsStarted=true")
    List<Candidate> findAllActiveAssessmentCandidates();

    @Query("SELECT new com.iris.OnlineCompilerBackend.dtos.CandidateDetailsResDTO(c.candidateId,c.candidateFullName,c.technology,c.yearsOfExperience,c.candidateEmailId) " +
            "FROM Candidate c " +
            "WHERE c.urlIsActive=true AND c.urlHashCode=:hashCode")
    CandidateDetailsResDTO findDetailsByUrl(@Param("hashCode") String hashCode);

    @Query("SELECT new com.iris.OnlineCompilerBackend.dtos.CandidateDetailsResDTO(c.candidateId,c.candidateFullName,c.technology,c.yearsOfExperience,c.candidateEmailId) " +
            "FROM Candidate c " +
            "WHERE c.urlCreatedTime = " +
            "(SELECT MAX(c2.urlCreatedTime) " +
            "FROM Candidate c2 " +
            "WHERE c2.candidateId=c.candidateId) " +
            "ORDER BY c.candidateFullName")
    List<CandidateDetailsResDTO> findAllCandidates();

    @Query("SELECT new com.iris.OnlineCompilerBackend.dtos.SubmissionsResDTO(c.candidateId,c.candidateFullName,c.interviewerId,s.time,s.code,s.output,s.action,s.status) FROM CodeSnippet s " +
            "LEFT JOIN Candidate c ON c.userId=s.userIdFk.userId")
    List<SubmissionsResDTO> findAllSubmissions();

    @Query("SELECT new com.iris.OnlineCompilerBackend.dtos.SubmissionsResDTO(c.candidateId,c.candidateFullName,c.interviewerId,s.time,s.code,s.output,s.action,s.status) " +
            "FROM CodeSnippet s " +
            "LEFT JOIN Candidate c ON c.userId=s.userIdFk.userId " +
            "WHERE c.interviewerId=:interviewerId")
    List<SubmissionsResDTO> findAllSubmissionsByInterviewerId(@Param("interviewerId") String interviewerId);

    @Query("SELECT new com.iris.OnlineCompilerBackend.dtos.CandidateDetailsResDTO(c.candidateId,c.candidateFullName,c.technology,c.yearsOfExperience,c.candidateEmailId) " +
            "FROM Candidate c " +
            "WHERE c.candidateEmailId =:candidateEmailId " +
            "ORDER BY c.urlExpiryTime DESC " +
            "LIMIT 1")
    CandidateDetailsResDTO findByCandidateEmailIdAndLatest(@Param("candidateEmailId") String candidateEmailId);

    @Query("FROM Candidate c " +
            "WHERE c.urlIsActive=true")
    List<Candidate> findAllActiveCandidateDetails();

    @Query("FROM Candidate c " +
            "WHERE c.interviewerId=:adminId " +
            "AND " +
            "c.urlIsActive=true")
    List<Candidate> findAllActiveAssessmentsByAdminId(@Param("adminId") String adminId);

    @Query("SELECT c.url " +
            "FROM Candidate c " +
            "WHERE c.candidateId=:candidateId " +
            "AND " +
            "c.urlIsActive=true")
    String findActiveUrlById(@Param("candidateId") String candidateId);
}
