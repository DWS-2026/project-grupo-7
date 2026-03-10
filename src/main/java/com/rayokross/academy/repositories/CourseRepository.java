package com.rayokross.academy.repositories;
import  java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rayokross.academy.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByLevel(String level);
}