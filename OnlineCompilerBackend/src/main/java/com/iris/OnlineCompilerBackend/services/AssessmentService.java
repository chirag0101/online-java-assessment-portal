package com.iris.OnlineCompilerBackend.services;

import com.iris.OnlineCompilerBackend.config.AESV2;
import com.iris.OnlineCompilerBackend.constants.CodeTypes;
import com.iris.OnlineCompilerBackend.constants.CompilerActions;
import com.iris.OnlineCompilerBackend.constants.ReviewStatus;
import com.iris.OnlineCompilerBackend.dtos.AssessmentReportDTO;
import com.iris.OnlineCompilerBackend.dtos.ReportReviewDTO;
import com.iris.OnlineCompilerBackend.dtos.request.*;
import com.iris.OnlineCompilerBackend.dtos.response.*;
import com.iris.OnlineCompilerBackend.exceptions.GlobalException;
import com.iris.OnlineCompilerBackend.models.*;
import com.iris.OnlineCompilerBackend.repositories.*;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AssessmentService {
    private static final Logger log = LoggerFactory.getLogger(AssessmentService.class);
    @Autowired
    private CandidateRepo candidateRepo;

    @Autowired
    private CodeSnippetRepo codeSnippetRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private CandidateMasterRepo candidateMasterRepo;

    @Autowired
    private AssessmentReportRepo assessmentReportRepo;

    //frontend-url
    @Value("${assessment.url}")
    private String assessmentUrl;

    public ApiResponse verifyUrl(String assessmentId) {
        try {

            if (assessmentId.contains(" ")) {
                assessmentId = assessmentId.replace(' ', '+');
            }

            //get candidate details by url
            CandidateDetailsResDTO candidateDetails = candidateRepo.findDetailsByUrl(assessmentId);

            if (candidateDetails == null) {
                return new ApiResponse.Builder().status(false).statusMessage("INVALID URL!").build();
            }

            //validate current time & scheduled time
            Date now = new Date();

            if (now.compareTo(candidateDetails.getInterviewDateTime()) < 0) {
                return new ApiResponse.Builder().status(false).statusMessage("INVALID URL!").build();
            }

            Candidate candidate = candidateRepo.findByCandidateIdAndUrl(candidateDetails.getCandidateId(), assessmentId);

            //if assessment isn't started
            if (!candidate.getAssessmentIsStarted()) {
                candidate.setAssessmentIsStarted(true);
                Date currentTime = new Date();
                candidate.setAssessmentStartTime(currentTime);
                candidate.setAssessmentEndTime(new Date(currentTime.getTime() + 120L * 60 * 1000));
                candidate.setUrlExpiryTime(candidate.getAssessmentEndTime());
                candidate.setStatus(ReviewStatus.TO_BE_REVIEWED.getStatus());
                candidate.setIsReviewed(false);
                candidate.setAssessmentIsEnded(false);
                candidate.setScore(null);
                candidateRepo.save(candidate);
            }

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(candidateDetails).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse createNewAssessment(NewCandidateReqDTO newCandidateReqDTO) {
        try {

            Date date = new Date();

            if (date.after(newCandidateReqDTO.getInterviewDateTime())) {
                throw new GlobalException("Assessment Date/Time can't be in the Past!");
            }

            //check if assessment is already going on for same round
            List<Candidate> listOfExistingAssessments = candidateRepo.findAllActiveAssessmentsForCandidate(newCandidateReqDTO.getCandidateId());

            for (Candidate c : listOfExistingAssessments) {
                if (c.getInterviewRound().equals(newCandidateReqDTO.getInterviewRound())) {
                    throw new GlobalException("Assessment undergoing for the same Round! To create new Assessment, first end the existing Assessment!");
                }
            }

            CandidateMaster candidateMaster = new CandidateMaster();
            Admin admin = adminRepo.findByAdminId(newCandidateReqDTO.getAdminId());

            if (admin.equals(null)) {
                throw new GlobalException("Admin Not Found!");
            }

            //if candidate doesn't exist add in Candidate Master
            if (candidateMasterRepo.findByCandidateEmail(newCandidateReqDTO.getCandidateEmailId()) == null) {
                candidateMaster.setCandidateId(newCandidateReqDTO.getCandidateId());
                candidateMaster.setCandidateName(newCandidateReqDTO.getCandidateFullName().toUpperCase());
                candidateMaster.setCandidateEmail(newCandidateReqDTO.getCandidateEmailId());
                candidateMaster.setCreatedBy(admin);
                candidateMaster.setCreatedOn(date);
                candidateMasterRepo.saveAndFlush(candidateMaster);
            } else {
                candidateMaster = candidateMasterRepo.findByCandidateEmail(newCandidateReqDTO.getCandidateEmailId());
            }

            //add candidate in Candidate table
            Candidate newCandidateEntry = new Candidate();

            newCandidateEntry.setCandidateIdFk(candidateMaster);
            newCandidateEntry.setCandidateIdCode(newCandidateReqDTO.getCandidateId().toUpperCase());
            newCandidateEntry.setInterviewerId(newCandidateReqDTO.getInterviewerId().toUpperCase());
            newCandidateEntry.setTechnology(newCandidateReqDTO.getCandidateTechnology().toUpperCase());
            newCandidateEntry.setUrlCreatedTime(date);
            newCandidateEntry.setUrlExpiryTime(new Date(date.getTime() + 120L * 60 * 1000));
            newCandidateEntry.setUrlHashCode((createAssessmentUrl(newCandidateReqDTO.getInterviewerId(), newCandidateReqDTO.getCandidateId(), date)));
            newCandidateEntry.setUrl(assessmentUrl.concat("?assessmentCode=" + (newCandidateEntry.getUrlHashCode())));
            newCandidateEntry.setUrlIsActive(true);
            newCandidateEntry.setAssessmentDateTime(newCandidateReqDTO.getInterviewDateTime());

            //todo setting yearsofexp in months to yrs
            Double yearsOfExperience = newCandidateReqDTO.getCandidateYearsOfExpInMonths() / 12.0;

            newCandidateEntry.setYearsOfExperience(newCandidateReqDTO.getCandidateYearsOfExpInMonths());
            newCandidateEntry.setAssessmentIsStarted(false);
            newCandidateEntry.setAssessmentStartTime(null);
            newCandidateEntry.setAssessmentEndTime(null);
            newCandidateEntry.setInterviewRound(newCandidateReqDTO.getInterviewRound().toUpperCase());
            newCandidateEntry.setIsReviewed(false);
            newCandidateEntry.setScore(null);
            newCandidateEntry.setRemarks(null);
            newCandidateEntry.setAssessmentIsEnded(false);

            newCandidateEntry.setAssessmentAssignedBy(admin);

            candidateRepo.save(newCandidateEntry);

            return new ApiResponse.Builder().status(true).response(newCandidateEntry.getUrl()).statusMessage("Assessment Created Successfully! Copy Assessment Url from below").build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).response(null).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse viewSubmissionByCandidateId(ViewCandidateSubmissionsReqDTO req) {
        try {
            Optional<Candidate> user = candidateRepo.findByCandidateDetails(req.getCandidateId(), req.getInterviewerId(), req.getAssessmentEndTime(), req.getRound());
            Candidate candidate;
            if (user.isPresent()) {
                candidate = user.get();
            } else {
                throw new GlobalException("Candidate Not Found");
            }

            if (candidate.getUserId() == null) {
                return new ApiResponse.Builder().status(false).response(null).statusMessage("CANDIDATE NOT FOUND!").build();
            }

            String langType = null;

            for (CodeTypes type : CodeTypes.values()) {
                if (type.getCodeType().equals(req.getLangType().toUpperCase())) {
                    langType = type.getCodeType();
                    break;
                }
            }

            if (langType == null) {
                throw new GlobalException("Invalid Language Type");
            }

            List<CodeResDTO> submissions = codeSnippetRepo.findSubmissionByUserIdAndLangType(candidate.getUserId(), langType);

            CandidateSubmissionResDTO candidateSubmissionResDTO = new CandidateSubmissionResDTO();
            candidateSubmissionResDTO.setCandidateId(req.getCandidateId());
            candidateSubmissionResDTO.setCodeSubmissions(submissions);
            candidateSubmissionResDTO.setCodeType(langType);
            candidateSubmissionResDTO.setCandidateName(candidate.getCandidateIdFk().getCandidateName());
            candidateSubmissionResDTO.setInterviewerId(candidate.getInterviewerId());

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
        String candidateName = candidate.getCandidateIdFk().getCandidateName() + " " + '(' + candidate.getCandidateIdFk().getCandidateId() + ')';

        return new ActiveAssessmentDetResDTO(candidateName, adminName, candidate.getYearsOfExperience(), candidate.getTechnology(), candidate.getInterviewRound(), candidate.getUrlExpiryTime(), candidate.getAssessmentStartTime(), candidate.getAssessmentEndTime(), candidate.getUrl(), candidate.getAssessmentDateTime(), candidate.getAssessmentIsStarted());
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

    public ApiResponse expireAssessment(ExpireAssessmentReqDTO req) {
        try {
            Candidate candidate = candidateRepo.findByCandidateIdAndAssessmentIsActiveAndAssessmentRound(req.getCandidateId(), req.getInterviewRound()).orElseThrow(() -> new Exception("NO ACTIVE ASSESSMENT"));
            candidate.setUrlIsActive(false);
            candidate.setAssessmentEndTime(new Date());
            candidate.setAssessmentIsEnded(true);
            candidate.setStatus(ReviewStatus.TO_BE_REVIEWED.getStatus());
            candidateRepo.save(candidate);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(null).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage("FAILURE").response(null).build();
        }
    }

    public ApiResponse extendCandidateAssessment(AssessmentExtensionReqDTO extensionReqDTO) {
        try {
            Candidate candidate = candidateRepo.findByCandidateIdIsActiveAndRound(extensionReqDTO.getCandidateId(), extensionReqDTO.getInterviewRound()).orElseThrow(() -> new Exception("CANDIDATE NOT FOUND"));
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
            List<CandidateDetailsResDTO> candidateDetailsResDTOS = candidateRepo.findAllCandidates();
            return new ApiResponse.Builder().status(true).response(candidateDetailsResDTOS).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).response(null).build();
        }
    }

    public ApiResponse fetchUserDetByEmail(String candidateEmail) {
        try {
            CandidateMaster candidate = candidateMasterRepo.findByCandidateEmail(candidateEmail);
            if (candidate == null) {
                return new ApiResponse.Builder().status(false).build();
            }
            CandidateDetailsResDTO candidateDetailsResDTO = candidateRepo.findByCandidateIdAndLatest(candidate.getCandidateId());
//            if (candidateDetailsResDTO == null) {
//            }
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

    public ApiResponse getAssessmentEndTimeByCandId(String candidateId) {
        try {
            Date time = candidateRepo.findAssessmentEndTimeByCandId(candidateId);
            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(time).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse candidatesStatus(String adminId) {
        try {
            Admin admin = adminRepo.findByAdminId(adminId);
            List<ViewCandidatesStatusResDTO> viewCandidatesStatusResDTO;
            if (admin.getIsAdmin() == true) {
                viewCandidatesStatusResDTO = candidateRepo.getCandidatesHistoryForMasterAdmin();
            } else {
                viewCandidatesStatusResDTO = candidateRepo.getCandidatesHistory(adminId);
            }
            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(viewCandidatesStatusResDTO).build();

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse submitData(CodeSnippetReqDTO codeSnippetReqDTO) {
        try {
            String assessmentId = codeSnippetReqDTO.getUrl();
            if (codeSnippetReqDTO.getUrl().contains(" ")) {
                assessmentId = assessmentId.replace(' ', '+');
            }

            Candidate candidate = candidateRepo.findByCandidateIdByUrlAndIsActive(codeSnippetReqDTO.getCandidateId(), assessmentId).orElseThrow(() -> new Exception("Candidate Not Found!"));

            List<String> assessmentCodeTypes = assessmentReportRepo.findAssessmentCodeTypesByUserId(candidate.getUserId());

            if (!assessmentCodeTypes.contains(CodeTypes.getCodeTypeById(codeSnippetReqDTO.getCodeType()))) {
                AssessmentReport assessmentReport = new AssessmentReport();
                assessmentReport.setCandidateUserIdFk(candidate);
                assessmentReport.setScore(null);
                assessmentReport.setComments(null);
                assessmentReport.setLangType(CodeTypes.getCodeTypeById(codeSnippetReqDTO.getCodeType()));
                assessmentReportRepo.save(assessmentReport);
            }

            // Saving code snippet to DB
            CodeSnippet codeSnippet = new CodeSnippet();
            codeSnippet.setCode(codeSnippetReqDTO.getCode());
            codeSnippet.setTime(new Date());
            codeSnippet.setStatus(null);
            codeSnippet.setOutput(null);
            codeSnippet.setAction(CompilerActions.SAVE.getAction());
            codeSnippet.setLanguageType(CodeTypes.getCodeTypeById(codeSnippetReqDTO.getCodeType()));
            codeSnippet.setUserIdFk(candidate);
            codeSnippetRepo.save(codeSnippet);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(null).build();
        } catch (Exception e) {
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse viewReport(ViewReportReqDTO req) {
        try {
            Boolean isAssessmentReviewed = candidateRepo.getIsAssessmentIsReviewed(req.getCandidateId(), req.getInterviewerId(), req.getAssessmentEndTime(), req.getRound());

            Optional<Candidate> candidateEntry = candidateRepo.findByCandidateDetails(req.getCandidateId(), req.getInterviewerId(), req.getAssessmentEndTime(), req.getRound());
            Candidate candidate;
            if (candidateEntry.isEmpty()) {
                throw new GlobalException("Candidate Not Found");
            } else {
                candidate = candidateEntry.get();
            }

            List<AssessmentReportDTO> assessmentReport = assessmentReportRepo.findAssessmentsReportByCandidateId(candidate.getUserId());

            AssessmentReportResDTO response = new AssessmentReportResDTO();
            response.setCandidateId(req.getCandidateId());
            response.setIsReviewed(isAssessmentReviewed);
            response.setAssessmentReports(assessmentReport);
            response.setFinalFeedback(candidate.getRemarks());
            response.setFinalAvgScore(candidate.getScore());
            response.setRound(candidate.getInterviewRound());
            response.setInterviewerId(candidate.getInterviewerId());
            response.setAssessmentEndTime(candidate.getAssessmentEndTime());
            response.setStatus(candidate.getStatus());

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(response).build();
        } catch (Exception e) {
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    public ApiResponse submitReport(SubmitReportReqDTO req) {
        try {
            Optional<Candidate> candidateInDb = candidateRepo.findByCandidateDetails(req.getCandidateId(), req.getInterviewerId(), req.getAssessmentEndTime(), req.getRound());
            Candidate candidate;
            if (candidateInDb.isPresent()) {
                candidate = candidateInDb.get();
            } else {
                throw new GlobalException("Candidate Not Found");
            }

            if (candidate.getIsReviewed()) {
                throw new GlobalException("Candidate Already Reviewed");
            }

            List<AssessmentReport> assessmentReports = assessmentReportRepo.findAssessmentReportByCandidateId(candidate.getUserId());

            List<ReportReviewDTO> reqReviews = req.getReviews();

            // Creating a map to find out languages
            Map<String, AssessmentReport> reportMap = assessmentReports.stream()
                    .collect(Collectors.toMap(
                            AssessmentReport::getLangType,
                            Function.identity()
                    ));

            for (ReportReviewDTO review : reqReviews) {
                AssessmentReport report = reportMap.get(review.getLangType().toUpperCase());
                if (report != null) {
                    report.setScore(review.getScore());
                    if (review.getFeedback() != null) {
                        report.setComments(review.getFeedback());
                    } else {
                        report.setComments(null);
                    }
                } else {
                    report = new AssessmentReport();
                    report.setLangType(review.getLangType().toUpperCase());
                    report.setCandidateUserIdFk(candidate);
                    report.setScore(review.getScore());
                    report.setComments(review.getFeedback());
                    assessmentReportRepo.save(report);
                }
            }

            if (req.getIsPassed()) {
                candidate.setStatus(ReviewStatus.PASSED.getStatus());
            } else {
                candidate.setStatus(ReviewStatus.FAILED.getStatus());
            }
            candidate.setIsReviewed(true);
            candidate.setRemarks(req.getFinalFeedback());
            candidate.setScore(req.getFinalAvgScore());

            candidateRepo.save(candidate);

            assessmentReportRepo.saveAll(assessmentReports);

            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(null).build();
        } catch (Exception e) {
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).build();
        }
    }

    @Scheduled(cron = "${assessment.expiry.scheduler.cron}")
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
