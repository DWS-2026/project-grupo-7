package com.rayokross.academy.controllers;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;
import com.rayokross.academy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.Optional;

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
    public String buyCourse(@PathVariable Long courseId, Principal principal, Model model) {

        if (principal == null) {
            log.warn("Buy-now attempt failed: User not authenticated.");
            return "redirect:/login";
        }

        String email = principal.getName();

        Optional<User> userOpt = userService.findByEmail(email);
        Optional<Course> courseOpt = courseService.findById(courseId);

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
}