package com.rayokross.academy.controllers.rest;

import java.time.LocalDateTime;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rayokross.academy.dtos.ErrorMessageDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RestErrorController implements ErrorController {

    @RequestMapping(value = "/error", produces = "application/json")
    public ResponseEntity<ErrorMessageDTO> handleApiError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String uriStr = (uri != null) ? uri.toString() : "Unknown URI";

        int statusCode = (status != null) ? Integer.parseInt(status.toString()) : 500;
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        String message = switch (httpStatus) {
            case NOT_FOUND -> "El recurso solicitado no existe.";
            case FORBIDDEN -> "Acceso denegado.";
            case UNAUTHORIZED -> "Debes estar autenticado para realizar esta acción.";
            default -> "Error interno del servidor.";
        };

        ErrorMessageDTO error = new ErrorMessageDTO(statusCode, message, uriStr, LocalDateTime.now());
        return new ResponseEntity<>(error, httpStatus);
    }
}