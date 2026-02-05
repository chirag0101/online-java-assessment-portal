package com.iris.OnlineCompilerBackend.exceptions;

import com.iris.OnlineCompilerBackend.models.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLTransientConnectionException;

@RestControllerAdvice
public class ExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(GlobalException.class)
    public ApiResponse globalExceptionHandler(GlobalException globalException) {
        log.info(globalException.getMessage());
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

    @org.springframework.web.bind.annotation.ExceptionHandler(DataAccessResourceFailureException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // 503, not 500
    public ApiResponse handleDataAccessFailure(DataAccessResourceFailureException e) {

        return new ApiResponse.Builder()
                .status(false)
                .response(null)
                .statusMessage("Database connection unavailable. Please try again later.")
                .build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SQLTransientConnectionException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResponse handleSqlTransient(SQLTransientConnectionException e) {

        return new ApiResponse.Builder()
                .status(false)
                .response(null)
                .statusMessage("Database is down or unreachable.")
                .build();
    }

}
