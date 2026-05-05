package com.rayokross.academy.dtos;

import java.util.List;

public record CourseDetailDTO(
        Long id,
        String title,
        String description, // Texto enriquecido
        String level,
        double price,
        String syllabusFileName, // REQUISITO: Nombre del fichero en disco
        List<LessonDTO> lessons) {
}
