package com.rayokross.academy.controllers.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.CheckoutDTO;
import com.rayokross.academy.services.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(@RequestBody CheckoutDTO checkoutDTO, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            cartService.processApiCheckout(checkoutDTO.courseIds(), principal.getName());
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}