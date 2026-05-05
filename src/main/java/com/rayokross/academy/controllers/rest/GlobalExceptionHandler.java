package com.rayokross.academy.controllers.rest;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.rayokross.academy.dtos.ErrorMessageDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDTO> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        ErrorMessageDTO message = new ErrorMessageDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessageDTO> handleIllegalState(IllegalStateException ex, WebRequest request) {
        ErrorMessageDTO message = new ErrorMessageDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now());
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}