package com.rayokross.academy.controllers.rest;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rayokross.academy.dtos.CartDTO;
import com.rayokross.academy.mappers.CourseMapper;
import com.rayokross.academy.models.Course;
import com.rayokross.academy.services.CartService;
import com.rayokross.academy.services.CourseService;
import com.rayokross.academy.services.EnrollmentService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/")
    public ResponseEntity<CartDTO> getCart() {
        CartDTO dto = new CartDTO(
                courseMapper.toBasicDTOs(cartService.getCart()), 
                cartService.getTotalPrice(),
                cartService.getCart() == null || cartService.getCart().isEmpty());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/")
    public ResponseEntity<CartDTO> addToCart(@RequestBody Long courseId) {
        Optional<Course> courseOpt = courseService.findById(courseId);
        if (courseOpt.isPresent()) {
            cartService.addCourse(courseOpt.get());
            return getCart(); 
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<CartDTO> removeFromCart(@PathVariable Long courseId) {
        cartService.removeCourse(courseId);
        return getCart(); 
    }

    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
        }

        try {
            enrollmentService.checkoutCart(principal.getName(), cartService.getCart());
            cartService.clearCart();
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}