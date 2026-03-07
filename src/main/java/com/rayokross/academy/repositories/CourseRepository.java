package com.rayokross.academy.repositories;

import com.rayokross.academy.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}