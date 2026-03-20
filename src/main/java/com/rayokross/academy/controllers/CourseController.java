package com.rayokross.academy.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.UserService;
import com.rayokross.academy.models.User;

@Controller
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @PostMapping("/courses/{id}/buy")
    public String buyCourseNow(@PathVariable String id, Principal principal) {

        if (principal == null) {
            log.warn("Direct buy attempt failed: User not authenticated.");
            return "redirect:/login";
        }

        try {
            Long courseId = Long.parseLong(id);
            Optional<User> userOpt = userService.findByEmail(principal.getName());
            Optional<Course> courseOpt = courseService.findById(courseId);

            if (userOpt.isPresent() && courseOpt.isPresent()) {
                User user = userOpt.get();
                Course course = courseOpt.get();

                boolean alreadyEnrolled = user.getEnrollments().stream().anyMatch(e -> e.getCourse().getId().equals(course.getId()));

                if (!alreadyEnrolled) {
                    Enrollment newEnrollment = new Enrollment(user, course);
                    user.getEnrollments().add(newEnrollment);
                    userService.save(user);
                    log.info("User '{}' bought course ID {}.", user.getEmail(), courseId);
                }
                return "redirect:/profile";
            }
        } catch (NumberFormatException e) {
            return "redirect:/courses";
        }

        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}")
    public String showCourseDetails(@PathVariable String id, Model model, Principal principal) {
        try {
            Long courseId = Long.parseLong(id);
            Optional<Course> courseOpt = courseService.findById(courseId);

            if (courseOpt.isPresent()) {
                Course course = courseOpt.get();
                model.addAttribute("course", course);
                model.addAttribute("pageTitle", course.getTitle());

                boolean isEnrolled = false;
                boolean isAdmin = false;

                if (principal != null) {
                    Optional<User> userOpt = userService.findByEmail(principal.getName());
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        isAdmin = user.getRoles().contains("ADMIN") || user.getRoles().contains("ROLE_ADMIN");
                        isEnrolled = user.getEnrollments().stream().anyMatch(e -> e.getCourse().getId().equals(course.getId()));
                    }
                }

                boolean canPurchase = !isAdmin && !isEnrolled;

                model.addAttribute("isEnrolled", isEnrolled);
                model.addAttribute("isAdmin", isAdmin);
                model.addAttribute("canPurchase", canPurchase);

                return "courseDescription";
            } else {
                log.warn("Course with ID {} not found in database. Returning 404.", courseId);
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid course ID format: '{}'. Redirecting to courses list.", id);
            return "redirect:/courses";
        }
        return "404";
    }
}