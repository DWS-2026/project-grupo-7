package com.rayokross.academy.repositories;

import com.rayokross.academy.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
}
