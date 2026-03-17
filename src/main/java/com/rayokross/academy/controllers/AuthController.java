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

import com.rayokross.academy.models.User;
import com.rayokross.academy.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

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

        // 3. Activar el bloque {{#logout}} en Mustache si se cerró sesión
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
            model.addAttribute("errorFirstName", "El nombre es obligatorio.");
            hasErrors = true;
        }

        if (lastName.trim().isEmpty()) {
            model.addAttribute("errorLastName", "El apellido es obligatorio.");
            hasErrors = true;
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            model.addAttribute("errorEmail", "Por favor, introduce una dirección de correo válida.");
            hasErrors = true;
        } 
     
        else if (userService.existEmail(email)) {
            model.addAttribute("errorEmail", "Este email ya está registrado.");
            hasErrors = true;
        }

        if (password.length() < 8) {
            model.addAttribute("errorPassword", "La contraseña debe tener al menos 8 caracteres.");
            hasErrors = true;
        }

        if (terms == null) {
            model.addAttribute("errorTerms", "Debes aceptar los Términos y Condiciones.");
            hasErrors = true;
        }

        if (hasErrors) {
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

        return "redirect:/login";
    }
}