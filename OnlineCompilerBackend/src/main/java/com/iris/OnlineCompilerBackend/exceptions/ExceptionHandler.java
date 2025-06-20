package com.iris.OnlineCompilerBackend.exceptions;

import com.iris.OnlineCompilerBackend.models.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(GlobalException.class)
    public ApiResponse globalExceptionHandler(GlobalException globalException) {
        logger.info(globalException.getMessage());
        return new ApiResponse.Builder().status(false).statusMessage(globalException.getMessage()).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse methodArgumentValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {

        //get DTO validation message from exception which is thrown as response
        String errorMessage = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse(methodArgumentNotValidException.getMessage());

        return new ApiResponse.Builder().status(false).statusMessage(errorMessage).build();
    }
}
