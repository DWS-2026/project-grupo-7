package com.rayokross.academy.dtos;

import java.util.List;

public record CourseDetailDTO(
                Long id,
                String title,
                String description,
                String level,
                double price,
                String syllabusFileName,
                List<LessonDTO> lessons) {
}
