package com.rayokross.academy.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CartService;
import com.rayokross.academy.services.CourseService;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartCourses", cartService.getCart());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        model.addAttribute("isEmpty", cartService.getCart().isEmpty());
        model.addAttribute("pageTitle", "My Cart");
        return "cart";
    }

    
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long courseId) {
        Optional<Course> courseOpt = courseService.findById(courseId);
        
        if (courseOpt.isPresent()) {
            cartService.addCourse(courseOpt.get());
        }
        
        return "redirect:/cart";
    }
}