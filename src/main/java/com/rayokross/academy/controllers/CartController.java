package com.rayokross.academy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String showCart(Model model) {
        // Le pasamos el título para el header
        model.addAttribute("pageTitle", "My Cart");

        // Renderiza vuestro archivo cart.html
        return "cart";
    }
}