package com.iris.OnlineCompilerBackend.controllers;

import com.iris.OnlineCompilerBackend.dtos.request.CodeSnippetReqDTO;
import com.iris.OnlineCompilerBackend.dtos.response.CodeSnippetResDTO;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.services.AssessmentService;
import com.iris.OnlineCompilerBackend.services.CodeService;
import com.iris.OnlineCompilerBackend.services.CompileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/CompilerService")
public class CompilerController {

    private final Logger log = LoggerFactory.getLogger(CompilerController.class);

    @Autowired
    private CompileService compileService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/jdkVersion")
    public ApiResponse getJdkVersion() {
        return compileService.getJdkVersion();
    }

    @PostMapping("/candidateAction/{actionId}")
    public CodeSnippetResDTO processCode(@Valid @RequestBody CodeSnippetReqDTO codeSnippetReqDTO, @PathVariable(value = "actionId") Integer actionId) {
        try {
            return compileService.executeCompilerAction(codeSnippetReqDTO, actionId);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new CodeSnippetResDTO.Builder().status("FAILURE").statusMessage(e.getMessage()).build();
        }
    }

    @GetMapping("/getCode/{codeId}")
    public ApiResponse viewCode(@PathVariable(name = "codeId") Long codeId) {
        return codeService.fetchCodeById(codeId);
    }

    @PostMapping("/submitData")
    public ApiResponse submitData(@RequestBody @Valid CodeSnippetReqDTO req) {
        return assessmentService.submitData(req);
    }

}
