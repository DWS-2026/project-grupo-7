package com.rayokross.academy.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses/{id}")
    public String showCourseDetails(@PathVariable String id, Model model) {
        System.out.println("DEBUG: Recibida petición para el curso con ID: " + id);

        try {
            Long courseId = Long.parseLong(id);
            Optional<Course> courseOpt = courseService.findById(courseId);

            if (courseOpt.isPresent()) {
                Course course = courseOpt.get();
                model.addAttribute("course", course);
                model.addAttribute("pageTitle", course.getTitle());
                model.addAttribute("isEnrolled", false);
                return "courseDescription";
            } else {
                System.out.println("DEBUG: El curso con ID " + id + " no existe en la BD.");
            }
        } catch (NumberFormatException e) {
            System.out.println("DEBUG: Error, el ID '" + id + "' no es un número válido.");
            return "redirect:/courses";
        }
        return "404";
    }
}
