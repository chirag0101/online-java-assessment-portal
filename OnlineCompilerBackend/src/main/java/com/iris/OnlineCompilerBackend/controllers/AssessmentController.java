package com.iris.OnlineCompilerBackend.controllers;

import com.iris.OnlineCompilerBackend.dtos.AssessmentExtensionReqDTO;
import com.iris.OnlineCompilerBackend.dtos.NewCandidateReqDTO;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.services.AssessmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AssessmentService")
public class AssessmentController {

    private static final Logger log = LoggerFactory.getLogger(AssessmentController.class);

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/assessment")
    public ApiResponse verifyAssessmentUrl(@RequestParam("assessmentCode") String assessmentHashCode) {
        return assessmentService.verifyUrl(assessmentHashCode);
    }

    @PostMapping("/new-assessment")
    public ApiResponse newAssessment(@Valid @RequestBody NewCandidateReqDTO newCandidateReqDTO) {

        return assessmentService.createNewAssessment(newCandidateReqDTO);
    }

    @GetMapping("/active-assessments")
    public ApiResponse viewAllActiveAssessments() {
        try {
            return assessmentService.getAllActiveAssessments();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().statusMessage("FAILURE").response(null).build();
        }
    }

    @GetMapping("/interviewer-active-assessments")
    public ApiResponse viewActiveAssessmentsByAdminId(@RequestHeader("adminId") String adminId) {
        return assessmentService.getActiveAssessmentsByAdminId(adminId);
    }

    @GetMapping("/view-submissions/{candidate-id}")
    public ApiResponse viewSubmissionsOfCandidates(@PathVariable(value = "candidate-id") String candidateId) {
        return assessmentService.viewSubmissionByCandidateId(candidateId);
    }

    @GetMapping("/view-submissions-for-interviewer/{interviewer-id}")
    public ApiResponse viewAllSubmissionsByInterviewerId(@PathVariable(value = "interviewer-id") String interviewerId) {
        return assessmentService.viewAllSubmissionsByInterviewerId(interviewerId);
    }

    @GetMapping("/all-submissions")
    public ApiResponse viewAllSubmissions() {
        return assessmentService.viewAllSubmissions();
    }

    @PostMapping("/end-assessment/{candidate-id}")
    public ApiResponse endAssessment(@PathVariable(name = "candidate-id") String candidateId) {
        return assessmentService.expireAssessment(candidateId);
    }

    @PostMapping("/end-assessment-by-admin/{candidate-id}")
    public ApiResponse endAssessmentByAdmin(@PathVariable(name = "candidate-id") String candidateId) {
        return assessmentService.expireAssessment(candidateId);
    }

    @PostMapping("/extend-assessment")
    public ApiResponse extendAssessment(@Valid @RequestBody AssessmentExtensionReqDTO assessmentExtensionReqDTO) {
        return assessmentService.extendCandidateAssessment(assessmentExtensionReqDTO);
    }

    @GetMapping("/all-candidates")
    public ApiResponse getAllCandidates() {
        return assessmentService.fetchAllCandidates();
    }

    @GetMapping("/userdet-by-email/{candidate-email}")
    public ApiResponse getAdminDetByEmail(@PathVariable(name = "candidate-email") String candidateEmail) {
        return assessmentService.fetchAdminDetByEmail(candidateEmail);
    }

    @GetMapping("/candidate-url/{candidate-id}")
    public ApiResponse getCandidateActiveUrlById(@PathVariable(name = "candidate-id") String candidateId) {
        return assessmentService.fetchActiveAssessmentUrlByCandidateId(candidateId);
    }
}