package com.rayokross.academy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CourseService;

@Controller
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/admin/courses/new")
    public String createCourse(Course course, @RequestParam("imageFile") MultipartFile imageFile) throws Exception {
        courseService.save(course, imageFile);
        return "redirect:/courses";
    }
}