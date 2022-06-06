package com.schedule.team.controller;

import com.schedule.team.model.response.DefaultErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<DefaultErrorResponse> handleDefaultErrorResponseErrors(
            MethodArgumentNotValidException exception
    ) {
        return ResponseEntity
                .badRequest()
                .body(
                        new DefaultErrorResponse(
                                exception
                                        .getBindingResult()
                                        .getAllErrors()
                                        .stream()
                                        .map(ObjectError::getDefaultMessage)
                                        .toList()
                        )
                );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<DefaultErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        return ResponseEntity
                .badRequest()
                .body(
                        new DefaultErrorResponse(
                                exception
                                        .getConstraintViolations()
                                        .stream()
                                        .map(ConstraintViolation::getMessage)
                                        .toList()
                        )
                );
    }
}
