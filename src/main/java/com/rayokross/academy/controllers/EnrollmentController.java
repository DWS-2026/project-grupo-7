package com.rayokross.academy.controllers;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import com.rayokross.academy.repositories.CourseRepository;
import com.rayokross.academy.repositories.EnrollmentRepository;
import com.rayokross.academy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/cart/buy-now/{courseId}")
    public String buyCourse(@PathVariable Long courseId, Principal principal, Model model) {
        
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        Course course = courseRepository.findById(courseId).orElseThrow();

        //¿Ya lo ha comprado antes?
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByUserAndCourse(currentUser, course);
        
        if (existingEnrollment.isPresent()) {
            return "redirect:/profile?error=already_enrolled";
        }


        Enrollment newEnrollment = new Enrollment(currentUser, course);
        enrollmentRepository.save(newEnrollment);

        return "redirect:/profile?success=course_purchased";
    }
}