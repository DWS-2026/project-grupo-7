package com.rayokross.academy.controllers.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rayokross.academy.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {

        if (request.getParameter("error") != null) {
            model.addAttribute("error", true);

            // Extraemos la excepción que provocó el fallo desde la sesión
            HttpSession session = request.getSession(false);
            if (session != null) {
                AuthenticationException ex = (AuthenticationException) session
                        .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                if (ex != null) {
                    model.addAttribute("errorMessage", ex.getMessage());
                }
            }
        }

        if (request.getParameter("logout") != null) {
            model.addAttribute("logout", true);
        }

        model.addAttribute("pageTitle", "Log In");
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("pageTitle", "Register");
        model.addAttribute("logged", false);
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        try {

            userService.registerNewUser(firstName, lastName, email, password);
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            log.warn("Registration failed for email '{}': {}", email, e.getMessage());

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email", email);

            return "register";
        }
    }
}