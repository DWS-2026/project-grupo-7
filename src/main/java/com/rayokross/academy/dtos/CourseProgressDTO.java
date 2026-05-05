package com.rayokross.academy.dtos;

public record CourseProgressDTO(
        CourseDetailDTO course,
        boolean isCompleted,
        Long currentLessonId) {
}