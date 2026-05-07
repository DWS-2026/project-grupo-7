package com.rayokross.academy.dtos;

import jakarta.validation.constraints.NotBlank;

public record CourseBasicDTO(
        Long id,
        @NotBlank(message = "Title is required")
        String title,
        String level,
        double price,
        String creatorName,
        int studentCount) {
}
