package com.rayokross.academy.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.repositories.LessonRepository;

@Service
public class LessonService {

    private static final Logger log = LoggerFactory.getLogger(LessonService.class);

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseService courseService;

    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
        log.info("Lesson saved. ID: {}", lesson.getId());
    }

    public void deleteById(Long id) {
        lessonRepository.deleteById(id);
        log.info("Lesson with ID {} deleted.", id);
    }

    public Optional<Lesson> findById(Long id) {
        return lessonRepository.findById(id);
    }

    public Lesson addLessonToCourse(Long courseId, Lesson lesson) {
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

}