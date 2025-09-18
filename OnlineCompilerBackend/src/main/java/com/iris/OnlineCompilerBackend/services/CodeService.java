package com.iris.OnlineCompilerBackend.services;

import com.iris.OnlineCompilerBackend.dtos.CodeResDTO;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.models.CodeSnippet;
import com.iris.OnlineCompilerBackend.repositories.CodeSnippetRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CodeService {

    private final Logger log = LoggerFactory.getLogger(CodeService.class);

    @Autowired
    private CodeSnippetRepo codeSnippetRepo;

    public ApiResponse fetchCodeById(Long codeId) {
        try {
            CodeSnippet codeSnippet = codeSnippetRepo.findByCodeId(codeId).orElseThrow(() -> new Exception("CODE RESPONSE NOT FOUND!"));
            CodeResDTO codeResDTO = new CodeResDTO(codeSnippet.getCode(), codeSnippet.getStatus(), codeSnippet.getOutput(), codeSnippet.getAction());
            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(codeResDTO).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).response(null).build();
        }
    }
}
