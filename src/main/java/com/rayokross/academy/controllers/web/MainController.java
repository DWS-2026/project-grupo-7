package com.rayokross.academy.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rayokross.academy.services.CourseService;

@Controller
public class MainController {

    private static final int POPULAR_COURSES_LIMIT = 3;

    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public String showIndex(Model model) {

        Pageable topThree = PageRequest.of(0, POPULAR_COURSES_LIMIT);
        model.addAttribute("popularCourses", courseService.findAll(topThree).getContent());
        model.addAttribute("pageTitle", "Home");
        return "index";
    }
}