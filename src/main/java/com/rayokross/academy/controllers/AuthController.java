package com.rayokross.academy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.rayokross.academy.models.User;
import com.rayokross.academy.services.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; 
    }

    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("user") User user, 
            BindingResult bindingResult, 
            Model model) {

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("firstName")) {
                model.addAttribute("errorFirstName", bindingResult.getFieldError("firstName").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("lastName")) {
                model.addAttribute("errorLastName", bindingResult.getFieldError("lastName").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("email")) {
                model.addAttribute("errorEmail", bindingResult.getFieldError("email").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("password")) {
                model.addAttribute("errorPassword", bindingResult.getFieldError("password").getDefaultMessage());
            }
            return "register";
        }

        if (userService.existEmail(user.getEmail())) {
            model.addAttribute("errorEmail", "This email is already registered.");
            return "register";
        }

        user.setRoles(List.of("USER"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.save(user);

        return "redirect:/login";
    }
}