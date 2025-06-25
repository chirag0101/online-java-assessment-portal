package com.iris.OnlineCompilerBackend.services;

import com.iris.OnlineCompilerBackend.config.AESV2;
import com.iris.OnlineCompilerBackend.dtos.*;
import com.iris.OnlineCompilerBackend.models.Admin;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.models.Candidate;
import com.iris.OnlineCompilerBackend.repositories.AdminRepo;
import com.iris.OnlineCompilerBackend.repositories.CandidateRepo;
import com.iris.OnlineCompilerBackend.repositories.CodeSnippetRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AssessmentService {
    private static final Logger log = LoggerFactory.getLogger(AssessmentService.class);
    @Autowired
    private CandidateRepo candidateRepo;

    @Autowired
    private CodeSnippetRepo codeSnippetRepo;

    @Autowired
    private AdminRepo adminRepo;

    //frontend-url
    @Value("${assessment.url}")
    private String assessmentUrl;


    public ApiResponse verifyUrl(String assessmentId) {
        try {

            if (assessmentId.contains(" ")) {
                assessmentId = assessmentId.replace(' ', '+');
            }

            CandidateDetailsResDTO candidateDetails = candidateRepo.findDetailsByUrl(assessmentId);

            if (candidateDetails == null) {
                return new ApiResponse.Builder().status(false).statusMessage("INVALID URL!").build();
            }

            Candidate candidate = candidateRepo.findByCandidateIdLatestEntry(candidateDetails.getCandidateId()).orElseThrow(() -> new Exception("CANDIDATE NOT FOUND!"));

            if (!candidate.getAssessmentIsStarted()) {
                candidate.setAssessmentIsStarted(true);
                Date currentTime = new Date();
                candidate.setAssessmentStartTime(currentTime);
                candidate.setAssessmentEndTime(new Date(currentTime.getTime() + 120L * 60 * 1000));
            }

            candidateRepo.save(candidate);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(candidateDetails).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse createNewAssessment(NewCandidateReqDTO newCandidateReqDTO) {
        try {

            //if candidate exists, set it's url & assessement as inactive
            if (candidateRepo.findByCandidateIdLatestEntry(newCandidateReqDTO.getCandidateId()).isPresent()) {
                Candidate candidate = candidateRepo.findByCandidateIdLatestEntry(newCandidateReqDTO.getCandidateId()).get();

                candidate.setUrlIsActive(false);
                candidate.setAssessmentIsStarted(false);
                candidateRepo.save(candidate);
            }

            Date date = new Date();

            Candidate newCandidateEntry = new Candidate();

            newCandidateEntry.setCandidateId(newCandidateReqDTO.getCandidateId().toUpperCase());
            newCandidateEntry.setCandidateFullName(newCandidateReqDTO.getCandidateFullName().toUpperCase());

            newCandidateEntry.setInterviewerId(newCandidateReqDTO.getInterviewerId().toUpperCase());
            newCandidateEntry.setTechnology(newCandidateReqDTO.getCandidateTechnology().toUpperCase());

            newCandidateEntry.setUrlCreatedTime(date);
            newCandidateEntry.setUrlExpiryTime(new Date(date.getTime() + 120L * 60 * 1000));

            newCandidateEntry.setUrlHashCode((createAssessmentUrl(newCandidateReqDTO.getInterviewerId(), newCandidateReqDTO.getCandidateId(), date)));
            newCandidateEntry.setUrl(assessmentUrl.concat("?assessmentCode=" + (newCandidateEntry.getUrlHashCode())));
            newCandidateEntry.setUrlIsActive(true);

            newCandidateEntry.setYearsOfExperience(Math.round((newCandidateReqDTO.getCandidateYearsOfExpInMonths() / 12.0) * 10.0) / 10.0);   //months to years

            newCandidateEntry.setAssessmentIsStarted(false);
            newCandidateEntry.setAssessmentStartTime(null);
            newCandidateEntry.setAssessmentEndTime(null);

            newCandidateEntry.setCandidateEmailId(newCandidateReqDTO.getCandidateEmailId());

            newCandidateEntry.setInterviewRound(newCandidateReqDTO.getInterviewRound());

            Admin admin = adminRepo.findByAdminId(newCandidateReqDTO.getAdminId());
            newCandidateEntry.setAssessmentAssignedBy(admin);

            candidateRepo.save(newCandidateEntry);

            return new ApiResponse.Builder().status(true).response(newCandidateEntry.getUrl()).statusMessage("SUCCESS").build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).response(null).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse viewSubmissionByCandidateId(String candidateId) {
        try {
            Long userId = candidateRepo.findUserIdByCandidateId(candidateId);

            if (userId == null) {
                return new ApiResponse.Builder().status(false).response(null).statusMessage("CANDIDATE NOT FOUND!").build();
            }

            List<CodeResDTO> submissions = codeSnippetRepo.findSubmissionByUserId(userId);

            CandidateSubmissionResDTO candidateSubmissionResDTO = new CandidateSubmissionResDTO();
            candidateSubmissionResDTO.setCandidateId(candidateId);
            candidateSubmissionResDTO.setCodeSubmissions(submissions);

            return new ApiResponse.Builder().status(true).response(candidateSubmissionResDTO).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).response(null).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse viewAllSubmissions() {
        try {
            List<SubmissionsResDTO> submissionsResDTOS = candidateRepo.findAllSubmissions();

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(submissionsResDTOS).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).response(null).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse viewAllSubmissionsByInterviewerId(String interviewerId) {
        try {
            List<SubmissionsResDTO> submissions = candidateRepo.findAllSubmissionsByInterviewerId(interviewerId);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(submissions).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    private String createAssessmentUrl(String interviewerId, String candidateId, Date urlDate) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        // Format for yyyymmdd
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(urlDate);

        // Format for hhmmss (24-hour format)
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        String time = timeFormat.format(urlDate);

        String baseUrlContent = interviewerId + "_" + candidateId + "_" + date + "_" + time;

        return (AESV2.getInstance().encrypt(baseUrlContent));
    }

    public ApiResponse getAllActiveAssessments() {
        try {
            List<Candidate> candidates = candidateRepo.findAllActiveCandidateDetails();
            List<ActiveAssessmentDetResDTO> activeAssessmentDetResDTOS = new ArrayList<>();
            for (Candidate candidate : candidates) {
                ActiveAssessmentDetResDTO activeAssessmentDetResDTO = getActiveAssessmentDetResDTO(candidate);
                activeAssessmentDetResDTOS.add(activeAssessmentDetResDTO);
            }

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(activeAssessmentDetResDTOS).build();

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage("FAILURE").response(null).build();
        }
    }

    private ActiveAssessmentDetResDTO getActiveAssessmentDetResDTO(Candidate candidate) {
        Admin interviewerName = adminRepo.findByAdminId(candidate.getInterviewerId());

        String adminName = interviewerName.getFullName() + " " + '(' + candidate.getInterviewerId() + ')';
        String candidateName = candidate.getCandidateFullName() + " " + '(' + candidate.getCandidateId() + ')';

        return new ActiveAssessmentDetResDTO(candidateName, adminName, candidate.getYearsOfExperience(), candidate.getTechnology(), candidate.getInterviewRound(), candidate.getUrlExpiryTime(), candidate.getAssessmentStartTime(), candidate.getAssessmentEndTime(), candidate.getUrl());
    }

    public ApiResponse getActiveAssessmentsByAdminId(String adminId) {
        try {

            List<Candidate> candidates = candidateRepo.findAllActiveAssessmentsByAdminId(adminId);
            List<ActiveAssessmentDetResDTO> activeAssessmentDetResDTOS = new ArrayList<>();
            for (Candidate candidate : candidates) {
                ActiveAssessmentDetResDTO activeAssessmentDetResDTO = getActiveAssessmentDetResDTO(candidate);
                activeAssessmentDetResDTOS.add(activeAssessmentDetResDTO);
            }

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(activeAssessmentDetResDTOS).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).response(null).build();
        }
    }

    public ApiResponse expireAssessment(String candidateId) {
        try {
            Candidate candidate = candidateRepo.findByCandidateIdAndIsActive(candidateId).orElseThrow(() -> new Exception("NO ACTIVE ASSESSMENT"));
            candidate.setUrlIsActive(false);
            candidate.setAssessmentEndTime(new Date());
            candidateRepo.save(candidate);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(null).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage("FAILURE").response(null).build();
        }
    }

    public ApiResponse extendCandidateAssessment(AssessmentExtensionReqDTO extensionReqDTO) {
        try {
            Candidate candidate = candidateRepo.findByCandidateIdAndIsActive(extensionReqDTO.getCandidateId()).orElseThrow(() -> new Exception("CANDIDATE NOT FOUND"));
            candidate.setUrlExpiryTime(new Date(candidate.getAssessmentEndTime().getTime() + (extensionReqDTO.getMinutes() * 60L * 1000L)));
            candidate.setAssessmentEndTime(new Date(candidate.getAssessmentEndTime().getTime() + (extensionReqDTO.getMinutes() * 60L * 1000L)));
            candidate.setUrlIsActive(true);
            candidateRepo.save(candidate);

            return new ApiResponse.Builder().status(false).statusMessage("SUCCESS").response(null).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage("FAILURE").response(null).build();
        }
    }

    public ApiResponse fetchAllCandidates() {
        try {
            return new ApiResponse.Builder().status(true).response(candidateRepo.findAllCandidates()).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).response(null).build();
        }
    }

    public ApiResponse fetchAdminDetByEmail(String candidateEmail) {
        try {
            CandidateDetailsResDTO candidateDetailsResDTO = candidateRepo.findByCandidateEmailIdAndLatest(candidateEmail);
            if (candidateDetailsResDTO == null) {
                return new ApiResponse.Builder().status(false).build();
            }
            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(candidateDetailsResDTO).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse fetchActiveAssessmentUrlByCandidateId(String candidateId) {
        try {
            String url = candidateRepo.findActiveUrlById(candidateId);
            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(url).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    @Scheduled(cron = "${assessement.expiry.scheduler.cron}")
    private void validateActiveAssessments() {
        List<Candidate> activeCandidateAssessments = candidateRepo.findAllActiveAssessmentCandidates();
        for (Candidate candidate : activeCandidateAssessments) {
            if ((new Date().getTime()) >= (candidate.getUrlExpiryTime().getTime())) {
                candidate.setUrlIsActive(false);
                candidateRepo.save(candidate);
            }
        }
    }
}
