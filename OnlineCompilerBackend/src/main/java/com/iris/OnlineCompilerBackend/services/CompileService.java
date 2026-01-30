package com.iris.OnlineCompilerBackend.services;

import com.iris.OnlineCompilerBackend.constants.CodeTypes;
import com.iris.OnlineCompilerBackend.constants.CompilerActions;
import com.iris.OnlineCompilerBackend.dtos.request.CodeSnippetReqDTO;
import com.iris.OnlineCompilerBackend.dtos.response.CodeSnippetResDTO;
import com.iris.OnlineCompilerBackend.models.ApiResponse;
import com.iris.OnlineCompilerBackend.models.AssessmentReport;
import com.iris.OnlineCompilerBackend.models.Candidate;
import com.iris.OnlineCompilerBackend.models.CodeSnippet;
import com.iris.OnlineCompilerBackend.repositories.AssessmentReportRepo;
import com.iris.OnlineCompilerBackend.repositories.CandidateRepo;
import com.iris.OnlineCompilerBackend.repositories.CodeSnippetRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.util.Date;
import java.util.List;

@Service
public class CompileService {
    private static final Logger log = LoggerFactory.getLogger(CompileService.class);

    @Autowired
    private CodeSnippetRepo codeSnippetRepo;

    @Autowired
    private CandidateRepo candidateRepo;

    @Autowired
    private AssessmentReportRepo assessmentReportRepo;

    @Value("${assessment.programs.dir.path}")
    private String dirPath;

    public ApiResponse getJdkVersion() {
        try {
            String jdkVersion = System.getProperty("java.version");
            return new ApiResponse.Builder().status(true).statusMessage("SUCCESS").response(jdkVersion).build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse.Builder().status(false).statusMessage(e.getMessage()).response(null).build();
        }
    }

    public CodeSnippetResDTO executeCompilerAction(CodeSnippetReqDTO codeSnippetReqDTO, Integer actionId) throws Exception {

        //  running the code
        CodeSnippetResDTO codeSnippetResDTO = compileCode(codeSnippetReqDTO.getCandidateId(), codeSnippetReqDTO.getCode(), actionId);

        Candidate candidate = candidateRepo.findByCandidateIdByUrlAndIsActive(codeSnippetReqDTO.getCandidateId(), codeSnippetReqDTO.getUrl()).orElseThrow(() -> new Exception("Candidate Not Found!"));

        //checking if languageType exists for this candidate so that we can see it in the report
        List<String> assessmentCodeTypes=assessmentReportRepo.findAssessmentCodeTypesByUserId(candidate.getUserId());

        if(!assessmentCodeTypes.contains(CodeTypes.getCodeTypeById(codeSnippetReqDTO.getCodeType()))){
            AssessmentReport assessmentReport=new AssessmentReport();
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
        codeSnippet.setStatus(codeSnippetResDTO.getStatus());
        codeSnippet.setOutput(codeSnippetResDTO.getOutput());
        codeSnippet.setAction(CompilerActions.getActionById(actionId));
        codeSnippet.setLanguageType(CodeTypes.getCodeTypeById(codeSnippetReqDTO.getCodeType()));
        codeSnippet.setUserIdFk(candidate);
        codeSnippetRepo.save(codeSnippet);

        return codeSnippetResDTO;
    }

    private CodeSnippetResDTO compileCode(String candidateId, String code, Integer actionId) throws Exception {
        String className = getClassNameFromCode(code);

        String candidateDirPath=dirPath+File.separator+candidateId;

        File dir = new File(candidateDirPath);   //creating dir for candidate

        if (!dir.exists()) {
            dir.mkdir();
        }

        //file format: dirPath/candidateId/classname.java

        File sourceFile = new File(candidateDirPath + File.separator + className + ".java");

        //Writing code to the java file
        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(code);
        } catch (IOException e) {
            log.error("Error writing source file: {}", e.getMessage());
            return new CodeSnippetResDTO.Builder()
                    .status("FAILURE")
                    .statusMessage("Failed to write source file")
                    .build();
        }

        //getting java compiler's instance
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        //getting the jdk version
        //logger.info("JDK version: "+System.getProperty("java.version"));

        if (compiler == null) {

            log.info("Compiler not available!");

            return new CodeSnippetResDTO.Builder()
                    .status("FAILURE")
                    .statusMessage("Internal Server Error...")
                    .build();
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        //file manager to handle .class & .java files
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        //loading the source file
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(sourceFile);

        StringWriter compilerOutput = new StringWriter();

        //code compilation
        JavaCompiler.CompilationTask task = compiler.getTask(compilerOutput, fileManager, diagnostics, List.of("-Xlint:-options"), null, compilationUnits);
        boolean success = task.call();  //if code contains errors: the method returns false
        fileManager.close();

        //if any errors are thrown
        StringBuilder diagnosticMessages = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            diagnosticMessages.append(diagnostic.getKind())
                    .append(" on line ")
                    .append(diagnostic.getLineNumber())
                    .append(": ")
                    .append(diagnostic.getMessage(null))
                    .append("\n");
        }

        String compileOutput = compilerOutput.toString() + diagnosticMessages.toString();

        CodeSnippetResDTO.Builder builder = new CodeSnippetResDTO.Builder()
                .status(success ? "SUCCESS" : "FAILURE");

        //if any error occurs or if the compilerAction is just to COMPILE then return response
        if (!success || actionId.equals(CompilerActions.getIdByAction("COMPILE"))) {
            return builder.statusMessage(compileOutput).build();
        }

        // Run compiled class file
        try {
            String candidateFileDirPath = dirPath + File.separator + candidateId + "/";

            ProcessBuilder pb = new ProcessBuilder("java", "-cp", candidateFileDirPath, className);
            pb.redirectErrorStream(true); // merge stdout and stderr
            Process process = pb.start();

            StringBuilder runOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    runOutput.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();

            String finalOutput = runOutput.toString();
            builder.statusMessage(finalOutput);
            if (exitCode != 0) {
                builder.status("FAILURE");
                builder.statusMessage("Program exited with code " + exitCode + "\n" + finalOutput);
            }

        } catch (
                Exception e) {
            log.error("Runtime error: {}", e.getMessage());
            builder.status("FAILURE").statusMessage("Runtime error: " + e.getMessage());
        }

        return builder.build();
    }

//  method for fetching class name from code, on the basis of space and \n

    private String getClassNameFromCode(String code) throws Exception {
        try {
            String[] lines = code.split("\n");

            String filename = null;

            for (String line : lines) {
                String[] wordsArray = line.trim().split("\\s+");

                for (int iterator = 0; iterator < wordsArray.length - 2; iterator++) {
                    if (wordsArray[iterator].equals("public") && wordsArray[iterator + 1].equals("class")) {
                        filename = wordsArray[iterator + 2].trim();
                        return validateFileName(filename);
                    }
                }
            }
            return validateFileName(filename);
        } catch (Exception e) {
            throw new RuntimeException("Error: Invalid Class Name!");
        }
    }

    //validating if class name contains only A-Z , a-z , 0-9 , _ , $

    private String validateFileName(String filename) throws Exception {
        try {
            char[] fileNameArray = filename.toCharArray();
            StringBuilder validFileName = new StringBuilder();
            for (int fileNameIterator = 0; fileNameIterator < fileNameArray.length; fileNameIterator++) {
                if (((filename.charAt(fileNameIterator) >= 'A' && filename.charAt(fileNameIterator) <= 'Z')) || ((filename.charAt(fileNameIterator) >= 'a' && filename.charAt(fileNameIterator) <= 'z')) || ((filename.charAt(fileNameIterator) >= '0') && (filename.charAt(fileNameIterator) <= '9')) || (filename.charAt(fileNameIterator) == '_') || (filename.charAt(fileNameIterator) == '$')) {
                    validFileName.append(filename.charAt(fileNameIterator));
                } else {
                    return validFileName.toString();
                }
            }
            return validFileName.toString();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new Exception("Error: Invalid Class Name!");
        }
    }

}