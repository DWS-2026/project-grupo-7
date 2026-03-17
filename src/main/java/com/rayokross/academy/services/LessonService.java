package com.rayokross.academy.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.repositories.LessonRepository;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public void deleteById(Long id) {
        lessonRepository.deleteById(id);
    }

    public Optional<Lesson> findById(Long id) {
        return lessonRepository.findById(id);
    }
}