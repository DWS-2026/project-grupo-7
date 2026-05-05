package com.rayokross.academy.dtos;

import java.time.LocalDate;

public record EnrollmentDTO(
        Long id,
        Long userId,
        String userFullName,
        Long courseId,
        String courseTitle,
        LocalDate enrollmentDate,
        boolean completed) {
}
