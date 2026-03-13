package com.rayokross.academy.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.UserService;
import com.rayokross.academy.models.User;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @PostMapping("/courses/{id}/buy")
    public String buyCourseNow(@PathVariable String id, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Long courseId = Long.parseLong(id);
            Optional<User> userOpt = userService.findByEmail(principal.getName());
            Optional<Course> courseOpt = courseService.findById(courseId);

            if (userOpt.isPresent() && courseOpt.isPresent()) {
                User user = userOpt.get();
                Course course = courseOpt.get();

                boolean alreadyEnrolled = user.getEnrollments().stream()
                        .anyMatch(e -> e.getCourse().getId().equals(course.getId()));

                if (!alreadyEnrolled) {
                    Enrollment newEnrollment = new Enrollment(user, course);
                    user.getEnrollments().add(newEnrollment);
                    userService.save(user);
                }

                return "redirect:/profile";
            }
        } catch (NumberFormatException e) {
            return "redirect:/courses";
        }

        return "redirect:/courses";
    }

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
