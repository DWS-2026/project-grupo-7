package com.rayokross.academy.controllers;

import java.security.Principal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;

@Controller
public class EnrollmentController {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @PostMapping("/cart/buy-now/{courseId}")
    public String buyCourse(@PathVariable Long courseId, Principal principal) {

        if (principal == null) {
            log.warn("Buy-now attempt failed: User not authenticated.");
            return "redirect:/login";
        }

        String email = principal.getName();
        Optional<User> userOpt = userService.findByEmail(email);
        Optional<Course> courseOpt = courseService.findById(courseId);

        if (userOpt.isEmpty() || courseOpt.isEmpty()) {
            log.warn("Buy-now attempt failed: User or Course not found.");
            return "redirect:/courses";
        }

        User currentUser = userOpt.get();
        Course course = courseOpt.get();

        Optional<Enrollment> existingEnrollment = enrollmentService.findByUserAndCourse(currentUser, course);

        if (existingEnrollment.isPresent()) {
            return "redirect:/profile?error=already_enrolled";
        }

        Enrollment newEnrollment = new Enrollment(currentUser, course);
        enrollmentService.save(newEnrollment);

        log.info("User '{}' successfully purchased course ID {} via buy-now.", email, courseId);
        return "redirect:/profile?success=course_purchased";
    }

    @PostMapping("/courses/{id}/buy")
    public String buyCourseNow(@PathVariable Long id, Principal principal) {

        if (principal == null) {
            log.warn("Direct buy attempt failed: User not authenticated.");
            return "redirect:/login";
        }

        String email = principal.getName();
        Optional<User> userOpt = userService.findByEmail(email);
        Optional<Course> courseOpt = courseService.findById(id);

        if (userOpt.isEmpty() || courseOpt.isEmpty()) {
            log.warn("Direct buy attempt failed: User or Course not found for ID {}.", id);
            return "redirect:/courses";
        }

        User user = userOpt.get();
        Course course = courseOpt.get();

        Optional<Enrollment> existingEnrollment = enrollmentService.findByUserAndCourse(user, course);

        if (existingEnrollment.isPresent()) {
            log.info("User '{}' is already enrolled in course ID {}.", email, id);
            return "redirect:/profile";
        }

        Enrollment newEnrollment = new Enrollment(user, course);
        enrollmentService.save(newEnrollment);

        log.info("User '{}' bought course ID {}.", email, id);
        return "redirect:/profile";
    }

}