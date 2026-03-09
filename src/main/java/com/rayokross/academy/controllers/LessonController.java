package com.rayokross.academy.controllers;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LessonController {
    
    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/admin/courses/{courseId}/lessons/new")
    public String showNewLessonForm(@PathVariable Long courseId, Model model) {
        
        Course course = courseService.findById(courseId).orElseThrow();
        
        model.addAttribute("course", course);
        model.addAttribute("lesson", new Lesson());
        
        return "edit_course";
    }
    
    @PostMapping("/admin/courses/{courseId}/lessons/new")
    public String addLesson(@PathVariable Long courseId, Lesson lesson) {

        Course course = courseService.findById(courseId).orElseThrow();

        lesson.setCourse(course);
        
        lessonService.save(lesson);

        return "redirect:/admin/courses/" + courseId + "/edit";
    }
}