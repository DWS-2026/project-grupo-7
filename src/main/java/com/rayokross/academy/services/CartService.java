package com.rayokross.academy.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rayokross.academy.models.Course;

@Service
@SessionScope
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private List<Course> cartCourses = new ArrayList<>();

    public List<Course> getCart() {
        return cartCourses;
    }

    public void addCourse(Course course) {

        boolean alreadyExists = cartCourses.stream().anyMatch(c -> c.getId().equals(course.getId()));
        if (!alreadyExists) {
            cartCourses.add(course);
            log.info("Course ID {} added to the cart.", course.getId());
        }
    }

    public void removeCourse(Long courseId) {
        boolean removed = cartCourses.removeIf(course -> course.getId().equals(courseId));
        if (removed) {
            log.info("Course ID {} removed from cart.", courseId);
        }
    }

    public double getTotalPrice() {
        return cartCourses.stream().mapToDouble(Course::getPrice).sum();
    }

    public void clearCart() {
        cartCourses.clear();
        log.info("Session cart cleared.");
    }

    public int getSize() {
        return cartCourses.size();
    }
}