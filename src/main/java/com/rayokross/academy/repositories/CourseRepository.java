package com.rayokross.academy.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rayokross.academy.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Para mostrar los cursos destacados en index
    List<Course> findByLevel(String level);

    // para mostrar los cursos en el catálogo por niveles
    Page<Course> findByLevel(String level, Pageable pageable);
}