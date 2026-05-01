package com.rayokross.academy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping(value = "/error", produces = "text/html")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        model.addAttribute("pageTitle", "Oops, something went wrong");

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                log.warn("Error 404: Page not found - URI: {}", requestUri);
                model.addAttribute("errorMessage",
                        "Sorry, the page you are looking for does not exist or has been moved.");
                return "error/404";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log.error("Error 500: Internal server error - URI: {}", requestUri);
                model.addAttribute("errorMessage", "An internal server error has occurred. We are working on it.");
                return "error/500";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                log.warn("Error 403: Access denied - URI: {}", requestUri);
                model.addAttribute("errorMessage", "You do not have sufficient permissions to access this section.");
                return "error/403";

            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                log.warn("Error 400: Bad request or malformed URL - URI: {}", requestUri);
                model.addAttribute("errorMessage", "The address you entered is invalid or has an incorrect format.");
                return "error/400";
            }
        }

        model.addAttribute("errorMessage", "An unexpected error has occurred.");
        return "error/generic";
    }
}