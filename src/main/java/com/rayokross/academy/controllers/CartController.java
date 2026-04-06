package com.rayokross.academy.controllers;

import java.security.Principal;
import java.util.List;
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
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.models.User;
import com.rayokross.academy.services.CartService;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.UserService;

@Controller
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @PostMapping("/cart/checkout")
    public String checkout(Principal principal) {
        if (principal == null) {
            log.warn("Checkout failed: User not authenticated.");
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        User user = userOpt.get();

        List<Course> cartCourses = cartService.getCart();
        if (cartCourses.isEmpty()) {
            return "redirect:/cart";
        }

        for (Course course : cartCourses) {
            boolean alreadyEnrolled = false;

            for (Enrollment e : user.getEnrollments()) {
                if (e.getCourse().getId().equals(course.getId())) {
                    alreadyEnrolled = true;
                    break;
                }
            }

            if (!alreadyEnrolled) {
                Enrollment newEnrollment = new Enrollment(user, course);
                user.getEnrollments().add(newEnrollment);
            }
        }

        userService.save(user);
        cartService.clearCart();
        log.info("Checkout successful for user '{}'.", user.getEmail());

        return "redirect:/profile";
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