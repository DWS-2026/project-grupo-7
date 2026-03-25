package com.rayokross.academy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Lesson;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.LessonService;

@Controller
public class LessonController {

    private static final Logger log = LoggerFactory.getLogger(AdminCourseController.class);

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/admin/courses/{courseId}/lessons/new")
    public String addLesson(@PathVariable Long courseId, Lesson lesson) {

        Course course = courseService.findById(courseId).orElseThrow();

        lesson.setCourse(course);

        lessonService.save(lesson);

        return "redirect:/admin/courses/" + courseId + "/edit";
    }

    @PostMapping("/admin/courses/{courseId}/lessons/{id}/delete")
    public String deleteLesson(@PathVariable Long courseId, @PathVariable Long id, Model model) {
        lessonService.deleteById(id);
        log.info("Admin deleted lesson ID {} from course ID {}", id, courseId);
        return "redirect:/admin/courses/" + courseId + "/edit";
    }

}