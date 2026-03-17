package com.rayokross.academy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rayokross.academy.models.User;
import com.rayokross.academy.services.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("pageTitle", "Login");
        model.addAttribute("logged", false);
        if (error != null) {
            model.addAttribute("loginError", "Email o contraseña incorrectos. Por favor, inténtalo de nuevo.");
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
    public String processRegistration(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,
            @RequestParam String password,
            Model model) {

        if (password.length() < 8) {
            model.addAttribute("errorPassword", "La contraseña debe tener al menos 8 caracteres.");
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email", email);
            return "register"; 
        }

        if (userService.existEmail(email)) {
            model.addAttribute("errorEmail", "This email is already registered.");
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            return "register";
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRoles(List.of("USER"));
        user.setPassword(passwordEncoder.encode(password));
        userService.save(user);

        return "redirect:/login";
    }
}