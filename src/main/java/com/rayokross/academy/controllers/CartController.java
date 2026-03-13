package com.rayokross.academy.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rayokross.academy.models.Course;
import com.rayokross.academy.models.Enrollment;
import com.rayokross.academy.services.CartService;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.UserService;
import com.rayokross.academy.models.User;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @PostMapping("/cart/checkout")
    public String checkout(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        User user = userOpt.get();

        var cartCourses = cartService.getCart();
        if (cartCourses.isEmpty()) {
            return "redirect:/cart";
        }

        for (Course course : cartCourses) {
            boolean alreadyEnrolled = user.getEnrollments().stream()
                    .anyMatch(e -> e.getCourse().getId().equals(course.getId()));

            if (!alreadyEnrolled) {
                Enrollment newEnrollment = new Enrollment(user, course);
                user.getEnrollments().add(newEnrollment);
            }
        }

        userService.save(user);

        cartService.clearCart();

        return "redirect:/profile";
    }

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

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long courseId) {
        // Llamamos al servicio para que borre el curso de la sesión
        cartService.removeCourse(courseId);

        // Redirigimos de vuelta a la vista del carrito para que se actualice la
        // pantalla
        return "redirect:/cart";
    }
}