package com.rayokross.academy.controllers.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rayokross.academy.services.UserService;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout) {

        model.addAttribute("pageTitle", "Login");

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