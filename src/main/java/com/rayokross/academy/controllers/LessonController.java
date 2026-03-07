package com.rayokross.academy.controllers;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.repositories.CourseRepository;
import com.rayokross.academy.repositories.LessonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LessonController {
    
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("admin/courses/{courseId}/lessons/new")
    public String showNewLessonForm(@PathVariable Long courseId, Model model) {
        return new String();
    }
    

}
