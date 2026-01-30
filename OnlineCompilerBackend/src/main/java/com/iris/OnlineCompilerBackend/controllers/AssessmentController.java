package com.iris.OnlineCompilerBackend.controllers;

import com.iris.OnlineCompilerBackend.dtos.request.*;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.services.AssessmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/AssessmentService")
public class AssessmentController {

    private static final Logger log = LoggerFactory.getLogger(AssessmentController.class);

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/assessment")
    public ApiResponse verifyAssessmentUrl(@RequestParam("assessmentCode") String assessmentHashCode) {
        return assessmentService.verifyUrl(assessmentHashCode);
    }

    @PostMapping("/newAssessment")
    public ApiResponse newAssessment(@Valid @RequestBody NewCandidateReqDTO newCandidateReqDTO) {

        return assessmentService.createNewAssessment(newCandidateReqDTO);
    }

    @GetMapping("/activeAssessments")
    public ApiResponse viewAllActiveAssessments() {
            return assessmentService.getAllActiveAssessments();
    }

    @GetMapping("/interviewerActiveAssessments")
    public ApiResponse viewActiveAssessmentsByAdminId(@RequestHeader("adminId") String adminId) {
        return assessmentService.getActiveAssessmentsByAdminId(adminId);
    }

//    @GetMapping("/view-submissions/{candidate-id}")
//    public ApiResponse viewSubmissionsOfCandidates(@PathVariable(value = "candidate-id") String candidateId) {
//        return assessmentService.viewSubmissionByCandidateId(candidateId);
//    }

    @PostMapping("/viewSubmissionsByCandidateId")
    public ApiResponse viewSubmissionsOfCandidates(@RequestBody ViewCandidateSubmissionsReqDTO req) {
        return assessmentService.viewSubmissionByCandidateId(req);
    }

    @GetMapping("/viewSubmissionsForInterviewer/{interviewerId}")
    public ApiResponse viewAllSubmissionsByInterviewerId(@PathVariable(value = "interviewerId") String interviewerId) {
        return assessmentService.viewAllSubmissionsByInterviewerId(interviewerId);
    }

    @GetMapping("/allSubmissions")
    public ApiResponse viewAllSubmissions() {
        return assessmentService.viewAllSubmissions();
    }

    @GetMapping("/candidatesStatus/{adminId}")
    public ApiResponse viewCandidatesStatus(@PathVariable(name = "adminId") String adminId) {
        return assessmentService.candidatesStatus(adminId);
    }

    @PostMapping("/endAssessment")
    public ApiResponse endAssessment(@RequestBody ExpireAssessmentReqDTO req) {
        return assessmentService.expireAssessment(req);
    }

    @PostMapping("/endAssessmentByAdmin")
    public ApiResponse endAssessmentByAdmin(@RequestBody ExpireAssessmentReqDTO req) {
        return assessmentService.expireAssessment(req);
    }

    @PostMapping("/extendAssessment")
    public ApiResponse extendAssessment(@Valid @RequestBody AssessmentExtensionReqDTO assessmentExtensionReqDTO) {
        return assessmentService.extendCandidateAssessment(assessmentExtensionReqDTO);
    }

    @GetMapping("/allCandidates")
    public ApiResponse getAllCandidates() {
        return assessmentService.fetchAllCandidates();
    }

    @GetMapping("/userDetByEmail/{candidateEmail}")
    public ApiResponse getUserDetByEmail(@PathVariable(name = "candidateEmail") String candidateEmail) {
        return assessmentService.fetchUserDetByEmail(candidateEmail);
    }

    @GetMapping("/candidateUrl/{candidateId}")
    public ApiResponse getCandidateActiveUrlById(@PathVariable(name = "candidateId") String candidateId) {
        return assessmentService.fetchActiveAssessmentUrlByCandidateId(candidateId);
    }

    @GetMapping("/assessmentEndTimeByCandidateId/{candidateId}")
    public ApiResponse getAssessmentEndTimeByCandidateId(@PathVariable(name = "candidateId") String candidateId) {
        return assessmentService.getAssessmentEndTimeByCandId(candidateId);
    }

    @PostMapping("/viewReport")
    public ApiResponse viewReport(@RequestBody ViewReportReqDTO req) {
        return assessmentService.viewReport(req);
    }

    @PostMapping("/submitReview")
    public ApiResponse submitReport(@RequestBody @Valid SubmitReportReqDTO submitReportReqDTO) {
        return assessmentService.submitReport(submitReportReqDTO);
    }
}