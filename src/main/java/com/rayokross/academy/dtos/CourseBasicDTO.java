package com.rayokross.academy.dtos;

public record CourseBasicDTO(
        Long id,
        String title,
        String level,
        double price,
        String creatorName,
        int studentCount) {
}
