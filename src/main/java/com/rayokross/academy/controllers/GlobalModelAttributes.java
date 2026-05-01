package com.rayokross.academy.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.rayokross.academy.services.CartService;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAttributes {

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request, Principal principal) {

        boolean logged = (principal != null);
        model.addAttribute("logged", logged);

        boolean admin = request.isUserInRole("ADMIN");
        model.addAttribute("admin", admin);

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("token", csrfToken.getToken());
        }

        if (!model.containsAttribute("pageTitle")) {
            model.addAttribute("pageTitle", "RayoKross Academy");
        }
        if (!model.containsAttribute("cartCount")) {
            model.addAttribute("cartCount", 0);
        }

        int cartCount = 0;
        try {
            if (cartService.getCart() != null) {
                cartCount = cartService.getCart().size();
            }
        } catch (Exception e) {
            cartCount = 0;
        }

        model.addAttribute("cartCount", cartCount);
        model.addAttribute("hasCartItems", cartCount > 0);
    }
}
