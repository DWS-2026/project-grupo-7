package com.rayokross.academy.controllers.rest;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.ErrorMessageDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDTO> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessageDTO> handleIllegalState(IllegalStateException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessageDTO> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return buildErrorResponse((HttpStatus) ex.getStatusCode(), ex.getReason(), request);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorMessageDTO> handleSecurityException(SecurityException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Acceso denegado: " + ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleGlobalException(Exception ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error inesperado en el servidor",
                request);
    }

    private ResponseEntity<ErrorMessageDTO> buildErrorResponse(HttpStatus status, String message, WebRequest request) {
        ErrorMessageDTO error = new ErrorMessageDTO(
                status.value(),
                message,
                request.getDescription(false),
                LocalDateTime.now());
        return new ResponseEntity<>(error, status);
    }
}