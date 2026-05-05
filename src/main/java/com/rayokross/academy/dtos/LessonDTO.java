package com.rayokross.academy.dtos;

public record LessonDTO(
        Long id,
        String title,
        String content,
        String videoUrl,
        int durationInMinutes) {
}
