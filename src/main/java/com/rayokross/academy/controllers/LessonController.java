package com.rayokross.academy.controllers;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.repositories.CourseRepository;
import com.rayokross.academy.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LessonController {
    
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    // 1. Corregida la barra inicial (/)
    @GetMapping("/admin/courses/{courseId}/lessons/new")
    public String showNewLessonForm(@PathVariable Long courseId, Model model) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        
        model.addAttribute("course", course);
        model.addAttribute("lesson", new Lesson());
        
        return "edit_course";
    }
    
    // 2. Limpieza de parámetros: Fuera BindingResult y Model
    @PostMapping("/admin/courses/{courseId}/lessons/new")
    public String addLesson(@PathVariable Long courseId, Lesson lesson) {

        Course course = courseRepository.findById(courseId).orElseThrow();

        lesson.setCourse(course);
        lessonRepository.save(lesson);

        return "redirect:/admin/courses/" + courseId + "/edit";
    }
}