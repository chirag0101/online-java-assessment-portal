package com.iris.OnlineCompilerBackend.controllers;

import com.iris.OnlineCompilerBackend.dtos.CodeSnippetReqDTO;
import com.iris.OnlineCompilerBackend.dtos.CodeSnippetResDTO;
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

    private final Logger logger = LoggerFactory.getLogger(CompilerController.class);

    @Autowired
    private CompileService compileService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/jdk-version")
    public ApiResponse getJdkVersion(){
        return compileService.getJdkVersion();
    }

    @PostMapping("/candidate-action/{action-id}")
    public CodeSnippetResDTO processCode(@Valid @RequestBody CodeSnippetReqDTO codeSnippetReqDTO, @PathVariable(value = "action-id") Integer actionId) {
        try {
            return compileService.executeCompilerAction(codeSnippetReqDTO, actionId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new CodeSnippetResDTO.Builder().status("FAILURE").statusMessage(e.getMessage()).build();
        }
    }

    @GetMapping("/get-code/{code-id}")
    public ApiResponse viewCode(@PathVariable(name = "code-id") Long codeId) {
        return codeService.fetchCodeById(codeId);
    }

}
