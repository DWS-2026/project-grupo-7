package com.rayokross.academy.dtos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record CourseDetailDTO(
        Long id,
        @NotBlank(message = "Title is required") String title,
        String description,
        String level,
        double price,
        String syllabusFileName,
        List<LessonDTO> lessons) {
}
