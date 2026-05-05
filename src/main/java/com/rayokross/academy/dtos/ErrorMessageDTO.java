package com.rayokross.academy.dtos;

import java.time.LocalDateTime;

public record ErrorMessageDTO(
        int statusCode,
        String message,
        String description,
        LocalDateTime timestamp) {
}
