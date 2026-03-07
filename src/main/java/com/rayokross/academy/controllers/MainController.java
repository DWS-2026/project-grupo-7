package com.rayokross.academy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
        // Los datos reales
        model.addAttribute("popularCourses", courseService.findAll());

        // Las variables que pide el header para no explotar
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("cartCount", 0);
        model.addAttribute("logged", false);

        return "index";
    }

    @GetMapping("/courses")
    public String showCatalog(Model model) {

        model.addAttribute("course", courseService.findAll());
        return "courses";

    }

}
