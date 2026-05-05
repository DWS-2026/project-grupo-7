package com.rayokross.academy.dtos;

public record UserRegistrationDTO(
        String firstName,
        String lastName,
        String email,
        String password) {
}