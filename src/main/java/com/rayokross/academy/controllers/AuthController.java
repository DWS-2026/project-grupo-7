package com.rayokross.academy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rayokross.academy.models.User;
import com.rayokross.academy.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm(Model model,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            HttpServletRequest request) {

        model.addAttribute("pageTitle", "Login");
        model.addAttribute("logged", false);

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("token", csrfToken.getToken());
        }

        if (error != null) {
            model.addAttribute("error", true);
        }

        if (logout != null) {
            model.addAttribute("logout", true);
        }

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
            @RequestParam(required = false) String terms,
            Model model) {

        boolean hasErrors = false;

        if (firstName.trim().isEmpty()) {
            model.addAttribute("errorFirstName", "The name is obligatory.");
            hasErrors = true;
        }

        if (lastName.trim().isEmpty()) {
            model.addAttribute("errorLastName", "The last name is obligatory.");
            hasErrors = true;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            model.addAttribute("errorEmail", "Please, introduce a valid email.");
            hasErrors = true;
        }
        else if (userService.existEmail(email)) {
            model.addAttribute("errorEmail", "This email is already registered.");
            log.warn("Registration attempt failed: Email '{}' is already registered.", email);
            hasErrors = true;
        }

        if (password.length() < 8) {
            model.addAttribute("errorPassword", "The password must contain 8 characters.");
            hasErrors = true;
        }

        if (terms == null) {
            model.addAttribute("errorTerms", "You must accept the terms and conditions.");
            hasErrors = true;
        }

        if (hasErrors) {
            log.warn("Registration failed for email '{}' due to validation errors.", email);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email", email);
            return "register";
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRoles(List.of("USER"));
        user.setPassword(passwordEncoder.encode(password));

        userService.save(user);

        log.info("New user registered successfully with email: '{}'", email);
        return "redirect:/login";
    }
}