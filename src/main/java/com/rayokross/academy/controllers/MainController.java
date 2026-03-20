package com.rayokross.academy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rayokross.academy.services.CourseService;

@Controller
public class MainController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public String showIndex(Model model) {
        Pageable topThree = PageRequest.of(0, 3);
        model.addAttribute("popularCourses", courseService.findAll(topThree).getContent());
        model.addAttribute("pageTitle", "Home");
        return "index";
    }
}