package com.rayokross.academy.controllers.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.rayokross.academy.services.EnrollmentService;

@Controller
public class EnrollmentController {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/cart/buy-now/{courseId}")
    public String buyCourse(@PathVariable Long courseId, Principal principal) {
        if (principal == null) {
            log.warn("Buy-now attempt failed: User not authenticated.");
            return "redirect:/login";
        }

        try {
            enrollmentService.enrollUser(principal.getName(), courseId);
            return "redirect:/profile?success=course_purchased";
        } catch (IllegalStateException e) {
            return "redirect:/profile?error=already_enrolled";
        } catch (IllegalArgumentException e) {
            return "redirect:/courses";
        }
    }

    @PostMapping("/courses/{id}/buy")
    public String buyCourseNow(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            log.warn("Direct buy attempt failed: User not authenticated.");
            return "redirect:/login";
        }

        try {
            enrollmentService.enrollUser(principal.getName(), id);
            return "redirect:/profile";
        } catch (IllegalStateException e) {
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            return "redirect:/courses";
        }
    }
}