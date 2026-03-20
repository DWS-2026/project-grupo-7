package com.rayokross.academy.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.repositories.LessonRepository;

@Service
public class LessonService {

    private static final Logger log = LoggerFactory.getLogger(LessonService.class);

    @Autowired
    private LessonRepository lessonRepository;

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
}