package com.rayokross.academy.controllers.rest;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rayokross.academy.dtos.CourseBasicDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;

@RestController
@RequestMapping("/api/v1") // Prefijo obligatorio según el enunciado
public class MainRestController {

    private static final int POPULAR_COURSES_LIMIT = 3;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * Proporciona los datos necesarios para la "Home" de la API.
     * En este caso, los 3 cursos populares que se ven en la web.
     */
    @GetMapping("/")
    public Collection<CourseBasicDTO> getPopularCourses() {
        // 1. Definimos el límite igual que en el controlador web[cite: 1, 4]
        Pageable topThree = PageRequest.of(0, POPULAR_COURSES_LIMIT);

        // 2. Obtenemos las entidades desde el Service[cite: 1, 4]
        List<Course> popularCourses = courseService.findAll(topThree).getContent();

        // 3. Convertimos las entidades a DTOs usando el mapper profesional
        // (MapStruct)[cite: 1, 4]
        return courseMapper.toBasicDTOs(popularCourses);
    }
}