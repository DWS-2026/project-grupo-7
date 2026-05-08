package com.rayokross.academy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rayokross.academy.models.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Page<Lesson> findByCourseId(Long courseId, Pageable pageable);
}