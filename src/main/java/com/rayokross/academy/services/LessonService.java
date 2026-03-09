package com.rayokross.academy.services;

import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}