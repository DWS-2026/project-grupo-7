package com.rayokross.academy.controllers.web;

import java.security.Principal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CartService;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;

@Controller
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService; 

    @PostMapping("/cart/checkout")
    public String checkout(Principal principal) {
        if (principal == null) {
            log.warn("Checkout failed: User not authenticated.");
            return "redirect:/login";
        }

        try {
            enrollmentService.checkoutCart(principal.getName(), cartService.getCart());

            cartService.clearCart();
            return "redirect:/profile";

        } catch (IllegalArgumentException e) {
            log.warn("Checkout error: {}", e.getMessage());
            return "redirect:/cart?error=checkout_failed";
        }
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartCourses", cartService.getCart());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        model.addAttribute("isEmpty", cartService.getCart() == null || cartService.getCart().isEmpty());
        model.addAttribute("pageTitle", "My Cart");
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long courseId) {
        Optional<Course> courseOpt = courseService.findById(courseId);
        if (courseOpt.isPresent()) {
            cartService.addCourse(courseOpt.get());
            log.info("Course ID {} added to cart.", courseId);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long courseId) {
        cartService.removeCourse(courseId);
        log.info("Course ID {} removed from cart.", courseId);
        return "redirect:/cart";
    }
}